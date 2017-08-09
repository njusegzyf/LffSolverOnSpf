package cn.nju.seg.atg.spfwrapper;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.symbc.SymbolicListener;

/**
 * When asked to solve a problem, an instance of this class only log inputs for Lff solver and return true,
 * which ensures that SPF will explore all paths.
 *
 * @author Zhang Yifan
 */
public final class ProblemLffDebug extends AbstractProblemLff {

  //endregion Instance fields

  @Override
  public Boolean solve() {

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

    this.logToFile(testClassName);

    // do not solve the problem and always return true.
    return Boolean.TRUE;
  }

  /**
   * Returns a fake value, since SPF will retrive values when {@link #solve()} method returns {@code Boolean.TRUE}.
   */
  @Override
  protected double getValValueByName(final String valName) {
    return 0.0;
  }
}
