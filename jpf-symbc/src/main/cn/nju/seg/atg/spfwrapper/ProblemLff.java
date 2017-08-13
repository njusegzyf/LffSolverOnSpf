package cn.nju.seg.atg.spfwrapper;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import cn.nju.seg.atg.parse.TestBuilder;
import cn.nju.seg.atg.spfwrapper.LffSolverConfigs.SymbolicConstraintsGeneralFunction;
import cn.nju.seg.atg.spfwrapper.SpfUtils.SplitPathCondition;
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

  private SymbolicConstraintsGeneral linearPartSolver = null;

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
    final String expression = this.expressionBuilder.toString();
    final String valName = this.valNameBuilder.toString();
    final String valType = this.valTypeBuilder.toString();

    this.appendToLogLines(testClassName, expression, valName, valType);

    final long beginSolveTime = System.currentTimeMillis();
    this.logLines.add("Parse time: " + (beginSolveTime - this.createTime) / 1000.0 + " seconds.");
    // since solve may fail, log these info here instead of after solving
    this.logToFile(testClassName);

    //region Use an extra solver to get a start point

    if (LffSolverConfigs.IS_USE_EXTRA_SOLVER_FOR_START_POINT) {
      final PathCondition pathConditionCopy = ProblemLff.this.pathCondition.make_copy();
      final SplitPathCondition splitPathCondition = SpfUtils.splitPathCondition(pathConditionCopy);
      final PathCondition linearPc = splitPathCondition.linearPc;
      final PathCondition nonLinearPc = splitPathCondition.nonLinearPc;

      if (!SpfUtils.isEmptyPathCondition(linearPc)) { // non-empty linear pc

        final AtomicReference<SymbolicConstraintsGeneral> linearPartSolverRef = new AtomicReference<>(null);
        final Boolean solveLinearPcResult =
            LffSolverConfigs.useExtraSymbolicConstraintsGeneral(new SymbolicConstraintsGeneralFunction<Boolean>() {
              @Override public Boolean apply(final SymbolicConstraintsGeneral extraSymbolicConstraintsGeneral) {

                linearPartSolverRef.set(extraSymbolicConstraintsGeneral);
                final boolean linearPcSolved = extraSymbolicConstraintsGeneral.solve(linearPc);

                if (linearPcSolved) {
                  // linear path condition is solved, set the solution as start point

                  // retrive the solution given by the extra solver
                  final RealVector p = ConcolicWalkSolver.makeVectorFromPcs(linearPc, nonLinearPc);

                  // container for start point, values should be ordered by `this.valNameToIndexMap`
                  final double[] startPoint = new double[p.space.dimensions().size()];

                  for (final Object variable : p.space.dimensions()) {
                    final String variableName = SpfUtils.getVariableName(variable);
                    final int index1 = ProblemLff.this.valNameToIndexMap.get(variableName);
                    assert index1 >= 0;
                    final int index2 = p.space.indexOf(variable);
                    assert index2 >= 0;

                    startPoint[index1] = p.values[index2];
                  }

                  // set start point
                  LffSolverUtils.setStartPoint(startPoint);

                  ProblemLff.this.logSolveLinearPathConditionRes(testClassName, true, startPoint);
                  return Boolean.TRUE;
                } else {
                  // linear path condition is not solved

                  // clear start point
                  LffSolverUtils.clearStartPoint();

                  ProblemLff.this.logSolveLinearPathConditionRes(testClassName, false, null);
                  return Boolean.FALSE;
                }
              }
            });

        this.linearPartSolver = linearPartSolverRef.get();

        if (!solveLinearPcResult.booleanValue()) {
          // if linear path condition is not solved, directly return false and do not use LFF solver
          return Boolean.FALSE;
        }
        // FIXME Skip using LFF solver when linear path condition is solved and non-linear path condition is empty.
        // To let the solution available in `TestBuilder` (so that `getValValueByName` works), we still use LFF solver.
        //        else {
        //          // if linear path condition is solved and non-linear path condition is empty, directly return true and do not use LFF solver
        //          if (SpfUtils.isEmptyPathCondition(nonLinearPc)){
        //            return Boolean.TRUE;
        //          }
        //        }

      } else { // empty linear pc
        ProblemLff.this.logLines.add("Skip empty linear path condition.");
        // clear start point
        LffSolverUtils.clearStartPoint();
      }
    }

    //endregion Use an extra solver to get a start point

    final Boolean solveResult = LffSolverUtils.solve(expression,
                                                     String.valueOf(this.id),
                                                     valType,
                                                     valName);

    final long endSolveTime = System.currentTimeMillis();
    this.logLines.add("Solve time: " + (endSolveTime - beginSolveTime) / 1000.0 + " seconds.");
    this.logLines.add(solveResult ? "Solved." : "Unsolved.");
    this.logToFile(testClassName);

    return solveResult;
  }

  @Override
  public double getRealValue(final Object dpVar) {
    if (dpVar instanceof String) {
      return super.getRealValue(dpVar);
    } else {
      assert this.linearPartSolver != null;

      return this.linearPartSolver.pb.getRealValue(dpVar);
    }
  }

  @Override
  public int getIntValue(final Object dpVar) {
    if (dpVar instanceof String) {
      return super.getIntValue(dpVar);
    } else {
      assert this.linearPartSolver != null;

      return this.linearPartSolver.pb.getIntValue(dpVar);
    }
  }

  @Override
  protected double getValValueByName(final String valName) {
    assert !Strings.isNullOrEmpty(valName);

    final int valIndex = this.valNameToIndexMap.get(valName).intValue();

    assert valIndex > -1; // assert that we find the variable
    assert TestBuilder.finalParams.length > valIndex;

    return TestBuilder.finalParams[valIndex];
  }

  private boolean logSolveLinearPathConditionRes(final String testClassName, final boolean isSolved, final double[] startPoint) {
    assert testClassName != null;
    assert !isSolved || startPoint != null;

    this.logLines.add("Solve linear part: " + (isSolved ? "Solved." : "Unsolved."));
    if (isSolved) {
      this.logLines.add("Start point: " + Arrays.toString(startPoint));
    }

    return this.logToFile(testClassName);
  }

  public void cleanup() {
  }

  //region Static fields

  private static final AtomicInteger idAcc = new AtomicInteger(0);

  private static final AtomicInteger lastActiveInstanceId = new AtomicInteger(-1);

  //endregion Static fields
}
