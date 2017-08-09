package cn.nju.seg.atg.spfwrapper;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Strings;

import cn.nju.seg.atg.parse.TestBuilder;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.symbc.SymbolicListener;

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

  //endregion Instance fields

  public ProblemLff() {
    this.id = ProblemLff.idAcc.getAndIncrement();
    ProblemLff.lastActiveInstanceId.set(this.id);
  }

  private void checkExclusive() {
    if (ProblemLff.lastActiveInstanceId.get() != this.id) {
      throw new IllegalStateException();
    }
  }

  @Override
  public Boolean solve() {
    this.checkExclusive();

    final JPF jpf = SymbolicListener.lastJpfInstance;
    assert jpf != null;
    final String testClassName = jpf.getReporter().getSuT();
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
