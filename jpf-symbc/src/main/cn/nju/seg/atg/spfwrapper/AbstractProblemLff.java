package cn.nju.seg.atg.spfwrapper;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Zhang Yifan
 */
public abstract class AbstractProblemLff extends IProblemLffParser {

  //region Instance fields

  protected final StringBuilder expressionBuilder = new StringBuilder();

  protected final StringBuilder valNameBuilder = new StringBuilder();

  protected final StringBuilder valTypeBuilder = new StringBuilder();

  private int nextValIndex = 0;

  protected final HashMap<String, Integer> valNameToIndexMap = Maps.newHashMap();

  protected final ArrayList<String> logLines = Lists.newArrayList();

  //endregion Instance fields

  @Override
  public void post(final Object constraint) {
    if (this.expressionBuilder.length() != 0) {
      this.expressionBuilder.append(" && ");
    }
    this.expressionBuilder.append('(');
    this.expressionBuilder.append(IProblemLffParser.castToExpInstance(constraint));
    this.expressionBuilder.append(')');
  }

  @Override
  public void postLogicalOR(final Object[] constraints) {
    for (final Object constraint : constraints) {
      assert constraint instanceof String;
    }

    final StringBuilder orResultBuilder = new StringBuilder("(");
    Joiner.on("||").appendTo(orResultBuilder, constraints);
    orResultBuilder.append(")");
    this.post(orResultBuilder.toString());
  }


  //region Mange variables

  @Override
  public String makeIntVar(final String name, final int min, final int max) {
    return this.makeVar(name, "int");
  }

  @Override
  public String makeRealVar(final String name, final double min, final double max) {
    return this.makeVar(name, "double");
  }

  private String makeVar(final String name, final String type) {
    if (this.valNameBuilder.length() != 0) {
      this.valNameBuilder.append(',');
      this.valTypeBuilder.append(',');
    }
    this.valNameBuilder.append(name);
    this.valTypeBuilder.append(type);

    // record the val's index
    this.valNameToIndexMap.put(name, Integer.valueOf(this.nextValIndex));
    ++this.nextValIndex;

    return name;
  }

  protected abstract double getValValueByName(final String valName);

  @Override
  public double getRealValue(final Object dpVar) {
    assert dpVar != null && dpVar instanceof String;

    final double dpVarValue = this.getValValueByName((String) dpVar);
    return dpVarValue;
  }

  @Override
  public int getIntValue(final Object dpVar) {
    assert dpVar != null && dpVar instanceof String;

    final double dpVarValue = this.getValValueByName((String) dpVar);
    return (int) dpVarValue;
  }

  @Override
  public double getRealValueInf(final Object dpvar) {
    return -1; // refer to `ProblemCoral`
  }

  @Override
  public double getRealValueSup(final Object dpVar) {
    return -1;
  }

  //endregion Mange variables

  protected final void appendToLogLines(final String testClassName, final String expression, final String valName, final String valType) {
    this.logLines.add("");
    this.logLines.add("Solve class: " + testClassName);

    this.logLines.add("Variable names:");
    this.logLines.add(valName);

    this.logLines.add("Variable types:");
    this.logLines.add(valType);

    this.logLines.add("Expressions:");
    this.logLines.add(expression);
  }

  protected final boolean logToFile(final String fileName) {
    assert !Strings.isNullOrEmpty(fileName);

    final boolean logRes = Utils.logToFile(LffSolverConfigs.LFF_LOG_DIR.resolve(fileName + ".txt"), this.logLines, true);
    this.logLines.clear();

    return logRes;
  }

  //region Static help methods

  public static boolean isProblemLff(final Object problemGeneral) {
    return problemGeneral instanceof AbstractProblemLff;
  }

  //endregion Static help methods
}
