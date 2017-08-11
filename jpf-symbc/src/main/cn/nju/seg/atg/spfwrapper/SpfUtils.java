package cn.nju.seg.atg.spfwrapper;

import static gov.nasa.jpf.symbc.concolic.walk.ConstraintIterator.eachConstraint;

import java.util.HashMap;
import java.util.HashSet;

import com.google.common.base.Preconditions;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.symbc.SymbolicListener;
import gov.nasa.jpf.symbc.concolic.walk.ConstraintSplitter;
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

  static class SplitPathCondition {

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

  private SpfUtils() {throw new UnsupportedOperationException();}
}
