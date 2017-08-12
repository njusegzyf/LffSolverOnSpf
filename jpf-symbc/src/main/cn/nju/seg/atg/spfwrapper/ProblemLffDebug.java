package cn.nju.seg.atg.spfwrapper;

/**
 * When asked to solve a problem, an instance of this class only log inputs for Lff solver and return true,
 * which ensures that SPF will explore all paths.
 *
 * @author Zhang Yifan
 */
public final class ProblemLffDebug extends AbstractProblemLff {

  @Override
  public Boolean solve() {

    final String testClassName = SpfUtils.getLastJpfTestClassName();
    final String expression = this.expressionBuilder.toString();
    final String valName = this.valNameBuilder.toString();
    final String valType = this.valTypeBuilder.toString();

    this.appendToLogLines(testClassName, expression, valName, valType);

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
