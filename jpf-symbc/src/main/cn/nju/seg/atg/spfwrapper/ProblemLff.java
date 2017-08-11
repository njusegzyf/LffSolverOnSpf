package cn.nju.seg.atg.spfwrapper;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import cn.nju.seg.atg.parse.TestBuilder;
import cn.nju.seg.atg.spfwrapper.SpfUtils.SplitPathCondition;
import cn.nju.seg.atg.util.ATG;
import gov.nasa.jpf.symbc.concolic.walk.ConcolicWalkSolver;
import gov.nasa.jpf.symbc.concolic.walk.RealVector;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicConstraintsGeneral;

/**
 * @author Zhang Yifan
 * @implNote Since the underlying LFF solver uses a lot static variables and thus can only be called once at a time,
 * so the instances of this class are mutually exclusive.
 * The instance field {@link #id} and static field {@link #lastActiveInstanceId} are used to do the check.
 */
public final class ProblemLff extends AbstractProblemLff {

  //region Instance fields

  private final int id;

  private final long createTime = System.currentTimeMillis();

  private final PathCondition pathCondition;

  //endregion Instance fields

  public ProblemLff(final PathCondition pathCondition) {
    Preconditions.checkNotNull(pathCondition);

    this.id = ProblemLff.idAcc.getAndIncrement();
    ProblemLff.lastActiveInstanceId.set(this.id);

    this.pathCondition = pathCondition;
  }

  private void checkExclusive() {
    if (ProblemLff.lastActiveInstanceId.get() != this.id) {
      throw new IllegalStateException();
    }
  }

  @Override
  public Boolean solve() {
    this.checkExclusive();

    final String testClassName = SpfUtils.getLastJpfTestClassName();
    this.logLines.add("");
    this.logLines.add("Solve class: " + testClassName);

    final String expression = this.expressionBuilder.toString();
    final String valName = this.valNameBuilder.toString();
    final String valType = this.valTypeBuilder.toString();

    this.logLines.add("Variable names:");
    this.logLines.add(valName);

    this.logLines.add("Variable types:");
    this.logLines.add(valType);

    this.logLines.add("Expressions:");
    this.logLines.add(expression);

    final long beginSolveTime = System.currentTimeMillis();
    this.logLines.add("Parse time: " + (beginSolveTime - this.createTime) / 1000.0 + " seconds.");

    //region Use an extra solver to get a start point

    if (LffSolverConfigs.IS_USE_EXTRA_SOLVER_FOR_START_POINT) {
      final SymbolicConstraintsGeneral extraSymbolicConstraintsGeneral = LffSolverConfigs.createExtraSymbolicConstraintsGeneral();
      final SplitPathCondition splitPathCondition = SpfUtils.splitPathCondition(this.pathCondition);
      final PathCondition linearPc = splitPathCondition.linearPc;
      final PathCondition nonLinearPc = splitPathCondition.nonLinearPc;
      final boolean linearPcSolved = extraSymbolicConstraintsGeneral.solve(splitPathCondition.linearPc);

      // linear path condition is solved, set the solution as start point
      if (linearPcSolved) {
        final RealVector p = ConcolicWalkSolver.makeVectorFromPcs(linearPc, nonLinearPc);
        // set start point
        final double[] startPoint = new double[p.space.dimensions().size()];
        for (final Object variable : p.space.dimensions()) {
          final String variableName = SpfUtils.getVariableName(variable);
          final int index1 = this.valNameToIndexMap.get(variableName);
          assert index1 >=0;
          final int index2 = p.space.indexOf(variable);
          assert index2 >=0;

          startPoint[index1] = p.values[index2];
        }
        ATG.CUSTOMIZED_PARAMS = startPoint;
      }
    }

    //endregion Use an extra solver to get a start point

    final Boolean solveResult = LffSolverUtils.solve(expression,
                                                     String.valueOf(this.id),
                                                     valType,
                                                     valName);

    final long endSolveTime = System.currentTimeMillis();
    this.logLines.add("Solve time: " + (endSolveTime - beginSolveTime) / 1000.0 + " seconds.");

    this.logToFile(testClassName);

    return solveResult;
  }

  @Override
  protected double getValValueByName(final String valName) {
    assert !Strings.isNullOrEmpty(valName);

    final int valIndex = this.valNameToIndexMap.get(valName).intValue();

    assert valIndex > -1; // assert that we find the variable
    assert TestBuilder.finalParams.length > valIndex;

    return TestBuilder.finalParams[valIndex];
  }

  //region Static fields

  private static final AtomicInteger idAcc = new AtomicInteger(0);

  private static final AtomicInteger lastActiveInstanceId = new AtomicInteger(-1);

  //endregion Static fields
}
