package cn.nju.seg.atg.spfwrapper;

import static gov.nasa.jpf.symbc.concolic.walk.ConstraintIterator.eachConstraint;
import static gov.nasa.jpf.symbc.concolic.walk.RealVectorSpace.labelDimension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

import org.eclipse.emf.ecore.xml.type.internal.RegEx;

import choco.cp.solver.constraints.global.geost.geometricPrim.Obj;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.symbc.SymbolicListener;
import gov.nasa.jpf.symbc.concolic.walk.ConstraintSplitter;
import gov.nasa.jpf.symbc.concolic.walk.RealVector;
import gov.nasa.jpf.symbc.concolic.walk.RealVectorSpace;
import gov.nasa.jpf.symbc.numeric.Constraint;
import gov.nasa.jpf.symbc.numeric.ConstraintExpressionVisitor;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.numeric.SymbolicReal;

/**
 * @author Zhang Yifan
 */
public final class SpfUtils {

  public static String getLastJpfTestClassName() {
    final JPF jpf = SymbolicListener.lastJpfInstance;
    if (jpf == null) {
      throw new IllegalStateException();
    }

    return jpf.getReporter().getSuT();
  }

  /**
   * Walks the PC and splits it into linearPc and nonLinearPc.
   *
   * @see gov.nasa.jpf.symbc.concolic.walk.ConcolicWalkSolver#solve(gov.nasa.jpf.symbc.numeric.PathCondition,
   * gov.nasa.jpf.symbc.numeric.SymbolicConstraintsGeneral)
   * @see gov.nasa.jpf.symbc.concolic.walk.ConstraintSplitter#splitInto(gov.nasa.jpf.symbc.numeric.PathCondition, gov.nasa.jpf.symbc.numeric.PathCondition,
   * gov.nasa.jpf.symbc.numeric.PathCondition)
   */
  static SplitPathCondition splitPathCondition(final PathCondition pc) {
    assert pc != null;

    final PathCondition linearPc = new PathCondition();
    final PathCondition nonLinearPc = new PathCondition();
    ConstraintSplitter.splitInto(pc, linearPc, nonLinearPc);

    return new SplitPathCondition(linearPc, nonLinearPc);
  }

  /**
   * Represents the result of splitting a `PathCondition` into a linear part and a non-linear part.
   */
  static final class SplitPathCondition {

    public final PathCondition linearPc;

    public final PathCondition nonLinearPc;

    /**
     * Package private constructor.
     */
    SplitPathCondition(final PathCondition linearPc, final PathCondition nonLinearPc) {
      assert linearPc != null && nonLinearPc != null;

      this.linearPc = linearPc;
      this.nonLinearPc = nonLinearPc;
    }
  }

  public static CollectVariableVisitor2 collectVariablesIn(final PathCondition pc) {

    final CollectVariableVisitor2 cvv = new CollectVariableVisitor2();
    for (Constraint constraint : eachConstraint(pc)) {
      constraint.accept(cvv);
    }
    return cvv;
  }

  /**
   * A visitor used to collect and store variables ({@link gov.nasa.jpf.symbc.numeric.SymbolicInteger} and {@link gov.nasa.jpf.symbc.numeric.SymbolicReal})
   * in a {@link gov.nasa.jpf.symbc.numeric.PathCondition}.
   *
   * @see gov.nasa.jpf.symbc.numeric.visitors.CollectVariableVisitor
   */
  public static final class CollectVariableVisitor2 extends ConstraintExpressionVisitor {

    public final HashSet<SymbolicReal> realVariables = new HashSet<>();

    public final HashSet<SymbolicInteger> integerVariables = new HashSet<>();

    public final HashMap<String, Integer> variableNameToIndexMap = new HashMap<>();

    private int nextVariableIndex = 0;

    @Override
    public void postVisit(final SymbolicReal realVariable) {
      assert realVariable != null;

      if (!this.realVariables.contains(realVariable)) {
        this.realVariables.add(realVariable);
        this.variableNameToIndexMap.put(realVariable.getName(), Integer.valueOf(this.nextVariableIndex));
        ++this.nextVariableIndex;
      }
    }

    @Override
    public void postVisit(SymbolicInteger integerVariable) {
      assert integerVariable != null;

      if (!this.integerVariables.contains(integerVariable)) {
        this.integerVariables.add(integerVariable);
        this.variableNameToIndexMap.put(integerVariable.getName(), Integer.valueOf(this.nextVariableIndex));
        ++this.nextVariableIndex;
      }
    }

    public int totalVariableCount() {
      assert this.variableNameToIndexMap.size() == this.realVariables.size() + this.integerVariables.size();

      return this.variableNameToIndexMap.size();
    }

    public int getIndex(final Expression expression) {
      Preconditions.checkNotNull(expression);

      if (expression instanceof SymbolicInteger) {
        return this.variableNameToIndexMap.get(((SymbolicInteger) expression).getName()).intValue();
      } else if (expression instanceof SymbolicReal) {
        return this.variableNameToIndexMap.get(((SymbolicReal) expression).getName()).intValue();
      } else {
        return -1;
      }
    }
  }

  public static String getVariableName(final Object variable) {
    Preconditions.checkNotNull(variable);

    if (variable instanceof SymbolicInteger) {
      return ((SymbolicInteger) variable).getName();
    } else if (variable instanceof SymbolicReal) {
      return ((SymbolicReal) variable).getName();
    } else {
      throw new IllegalArgumentException();
    }
  }

  public static boolean isEmptyPathCondition(final PathCondition pc) {
    Preconditions.checkNotNull(pc);

    return pc.header == null;
  }

  /**
   * Note: SPF index variables from 1.
   */
  public static int getIndexFromVarName(final String varName) {
    Preconditions.checkNotNull(varName);

    final boolean isFindIndex = varIndexMatcher.reset(varName).find();
    if (isFindIndex) {
      return Integer.parseInt(SpfUtils.varIndexMatcher.group(1));
    } else {
      return -1;
    }
  }

  private static final String varIndexPatternString = "_(\\d?\\d)_SYM";

  private static final Matcher varIndexMatcher = Pattern.compile(varIndexPatternString).matcher("");


  public static int getIndexFromDimension(final Object dimension) {
    Preconditions.checkNotNull(dimension);

    final String varName = RealVectorSpace.labelDimension(dimension);
    return SpfUtils.getIndexFromVarName(varName);
  }

  public static double[] orderSolutionValuesByVarIndex(final RealVector solutionVector) {
    int maxIndex = -1;
    final List<Object> dimensions = solutionVector.space.dimensions();
    for (final Object dimension : dimensions) {
      final int varIndex = SpfUtils.getIndexFromDimension(dimension);
      if (varIndex > maxIndex) {
        maxIndex = varIndex;
      }
    }

    final double[] solution = new double[maxIndex];
    for (int i = 0; i < dimensions.size(); ++i) {
      final int varIndex = SpfUtils.getIndexFromDimension(dimensions.get(i));
      solution[varIndex - 1] = solutionVector.values[i];
    }

    return solution;
  }

  @Deprecated
  private SpfUtils() { throw new UnsupportedOperationException(); }
}
