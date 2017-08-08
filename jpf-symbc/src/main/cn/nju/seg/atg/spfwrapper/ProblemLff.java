package cn.nju.seg.atg.spfwrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.nju.seg.atg.parse.TestBuilder;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.symbc.SymbolicListener;
import gov.nasa.jpf.symbc.numeric.solvers.ProblemGeneral;

/**
 * @author Zhang Yifan
 * @implNote Since the underlying LFF solver uses a lot static variables and thus can only be called once at a time,
 * so the instances of this class are mutually exclusive.
 * The instance field {@link #id} and static field {@link #lastActiveInstanceId} are used to do the check.
 */
public final class ProblemLff extends ProblemGeneral {

  //region Instance fields

  private final int id;

  private final StringBuilder expressionBuilder = new StringBuilder();

  private final StringBuilder valNameBuilder = new StringBuilder();

  private final StringBuilder valTypeBuilder = new StringBuilder();

  private int nextValIndex = 0;

  private final HashMap<String, Integer> valNameToIndexMap = Maps.newHashMap();

  private final long createTime = System.currentTimeMillis();

  private final ArrayList<String> logLines = Lists.newArrayList();

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
  public void post(final Object constraint) {
    if (this.expressionBuilder.length() != 0) {
      this.expressionBuilder.append(" && ");
    }
    this.expressionBuilder.append('(');
    this.expressionBuilder.append(ProblemLff.castToExpInstance(constraint));
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

  private void logToFile(final String fileName) {
    final Path logFile = ProblemLff.logDir.resolve(fileName + ".txt");
    try {
      Files.write(logFile, this.logLines, Charset.defaultCharset(),
                  StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    } catch (final IOException ignored) {}
  }

  //region Mange variables

  @Override
  public Object makeIntVar(final String name, final int min, final int max) {
    return this.makeVar(name, "int");
  }

  @Override public Object makeRealVar(final String name, final double min, final double max) {
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

  private double getValValueByName(final String valName) {
    assert !Strings.isNullOrEmpty(valName);

    final int valIndex = this.valNameToIndexMap.get(valName).intValue();

    assert valIndex > -1; // assert that we find the variable
    assert TestBuilder.finalParams.length > valIndex;

    return TestBuilder.finalParams[valIndex];
  }

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

  //region eq

  @Override
  public Object eq(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "==", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object eq(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "==", String.valueOf(value));
  }

  @Override
  public Object eq(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "==", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object eq(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "==", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object eq(final Object exp, final double value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "==", String.valueOf(value));
  }

  //endregion eq

  //region neq

  @Override
  public Object neq(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "!=", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object neq(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "!=", String.valueOf(value));
  }

  @Override
  public Object neq(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "!=", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object neq(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "!=", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object neq(final Object exp, final double value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "!=", String.valueOf(value));
  }

  //endregion neq

  //region leq

  @Override
  public Object leq(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "<=", ProblemLff.castToExpInstance(exp));
  }

  @Override public Object leq(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "<=", String.valueOf(value));
  }

  @Override
  public Object leq(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "<=", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object leq(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "<=", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object leq(final Object exp, final double value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "<=", String.valueOf(value));
  }

  //endregion leq

  //region geq

  @Override
  public Object geq(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), ">=", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object geq(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), ">=", String.valueOf(value));
  }

  @Override
  public Object geq(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), ">=", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object geq(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), ">=", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object geq(final Object exp, final double value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), ">=", String.valueOf(value));
  }

  //endregion geq

  //region lt

  @Override
  public Object lt(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "<", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object lt(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "<", String.valueOf(value));
  }

  @Override
  public Object lt(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "<", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object lt(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "<", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object lt(final Object exp, final double value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "<", String.valueOf(value));
  }

  //endregion lt

  //region gt

  @Override
  public Object gt(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), ">", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object gt(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), ">", String.valueOf(value));
  }

  @Override
  public Object gt(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), ">", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object gt(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), ">", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object gt(final Object exp, final double value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), ">", String.valueOf(value));
  }

  //endregion gt

  //region plus

  @Override
  public Object plus(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "+", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object plus(final Object exp, final int value) {
    return ProblemLff.toExprString(String.valueOf(exp), "+", String.valueOf(value));
  }

  @Override
  public Object plus(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "+", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object plus(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "+", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object plus(final Object exp, final double value) {
    return ProblemLff.toExprString(String.valueOf(exp), "+", String.valueOf(value));
  }

  //endregion plus

  //region minus

  @Override
  public Object minus(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "-", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object minus(final Object exp, final int value) {
    return ProblemLff.toExprString(String.valueOf(exp), "-", String.valueOf(value));
  }

  @Override
  public Object minus(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(String.valueOf(exp1), "-", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object minus(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "-", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object minus(final Object exp, final double value) {
    return ProblemLff.toExprString(String.valueOf(exp), "-", String.valueOf(value));
  }

  //endregion minus

  //region mult

  @Override
  public Object mult(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "*", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object mult(final Object exp, final int value) {
    return ProblemLff.toExprString(String.valueOf(exp), "*", String.valueOf(value));
  }

  @Override
  public Object mult(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(String.valueOf(exp1), "*", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object mult(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "*", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object mult(final Object exp, final double value) {
    return ProblemLff.toExprString(String.valueOf(exp), "*", String.valueOf(value));
  }

  //endregion mult

  //region div

  @Override
  public Object div(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "/", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object div(final Object exp, final int value) {
    return ProblemLff.toExprString(String.valueOf(exp), "/", String.valueOf(value));
  }

  @Override
  public Object div(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(String.valueOf(exp1), "/", ProblemLff.castToExpInstance(exp2));
  }

  @Override
  public Object div(final double value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "/", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object div(final Object exp, final double value) {
    return ProblemLff.toExprString(String.valueOf(exp), "/", String.valueOf(value));
  }

  //endregion div

  //region Math operations

  @Override
  public Object sin(Object exp) {
    return ProblemLff.toFunctionCallString("sin", exp);
  }

  @Override
  public Object cos(Object exp) {
    return ProblemLff.toFunctionCallString("cos", exp);
  }

  @Override
  public Object round(Object exp) {
    return ProblemLff.toFunctionCallString("round", exp);
  }

  @Override
  public Object exp(Object exp) {
    return ProblemLff.toFunctionCallString("exp", exp);
  }

  @Override
  public Object asin(Object exp) {
    return ProblemLff.toFunctionCallString("asin", exp);
  }

  @Override
  public Object acos(Object exp) {
    return ProblemLff.toFunctionCallString("acos", exp);
  }

  @Override
  public Object atan(Object exp) {
    return ProblemLff.toFunctionCallString("atan", exp);
  }

  @Override
  public Object log(Object exp) {
    return ProblemLff.toFunctionCallString("log", exp);
  }

  @Override
  public Object tan(Object exp) {
    return ProblemLff.toFunctionCallString("tan", exp);
  }

  @Override
  public Object sqrt(Object exp) {
    return ProblemLff.toFunctionCallString("exp", exp);
  }

  @Override
  public Object power(Object exp1, Object exp2) {
    return ProblemLff.toFunctionCallString("pow", exp1, exp2);
  }

  @Override
  public Object power(Object exp1, double exp2) {
    return ProblemLff.toFunctionCallString("pow", exp1, exp2);
  }

  @Override
  public Object power(double exp1, Object exp2) {
    return ProblemLff.toFunctionCallString("pow", exp1, exp2);
  }

  @Override
  public Object atan2(Object exp1, Object exp2) {
    return ProblemLff.toFunctionCallString("atan2", exp1, exp2);
  }

  @Override
  public Object atan2(Object exp1, double exp2) {
    return ProblemLff.toFunctionCallString("atan2", exp1, exp2);
  }

  @Override
  public Object atan2(double exp1, Object exp2) {
    return ProblemLff.toFunctionCallString("atan2", exp1, exp2);
  }

  //endregion Math operations

  //region bitwise and

  @Override
  public Object and(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "&", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object and(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "&", String.valueOf(value));
  }

  @Override
  public Object and(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "&", ProblemLff.castToExpInstance(exp2));
  }

  //endregion bitwise and

  //region bitwise or

  @Override
  public Object or(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "|", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object or(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "|", String.valueOf(value));
  }

  @Override
  public Object or(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "|", ProblemLff.castToExpInstance(exp2));
  }

  //endregion bitwise or

  //region bitwise xor

  @Override
  public Object xor(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "^", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object xor(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "^", String.valueOf(value));
  }

  @Override
  public Object xor(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "^", ProblemLff.castToExpInstance(exp2));
  }

  //endregion bitwise xor

  //region shift left

  @Override
  public Object shiftL(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), "<<", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object shiftL(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), "<<", String.valueOf(value));
  }

  @Override
  public Object shiftL(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "<<", ProblemLff.castToExpInstance(exp2));
  }

  //endregion shift left

  //region singed shift right

  @Override
  public Object shiftR(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), ">>", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object shiftR(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), ">>", String.valueOf(value));
  }

  @Override
  public Object shiftR(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), ">>", ProblemLff.castToExpInstance(exp2));
  }

  //endregion singed shift right

  //region unsigned shift right

  @Override
  public Object shiftUR(final int value, final Object exp) {
    return ProblemLff.toExprString(String.valueOf(value), ">>>", ProblemLff.castToExpInstance(exp));
  }

  @Override
  public Object shiftUR(final Object exp, final int value) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp), ">>>", String.valueOf(value));
  }

  @Override
  public Object shiftUR(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), ">>>", ProblemLff.castToExpInstance(exp2));
  }

  //endregion unsigned shift right

  @Override
  public Object mixed(final Object exp1, final Object exp2) {
    return ProblemLff.toExprString(ProblemLff.castToExpInstance(exp1), "=", ProblemLff.castToExpInstance(exp2));// refer to ProblemIAsolver
  }

  //region Static fields

  private static final AtomicInteger idAcc = new AtomicInteger(0);

  private static final AtomicInteger lastActiveInstanceId = new AtomicInteger(-1);

  // FIXME hard-coded log dir
  private static final Path logDir = Paths.get("/home/njuseg/experiments/gen/LffLogs");

  static {
    try {
      Files.createDirectories(logDir);
    } catch (final IOException ignored) {}
  }

  //endregion Static fields

  //region Static help methods

  private static String castToExpInstance(final Object exp) {
    return (String) exp;
  }

  private static String toExprString(final String left, final String op, final String right) {
    return "(" + left + op + right + ")";
  }

  private static String toFunctionCallString(final String functionName, final Object parameter) {
    return functionName + "(" + parameter.toString() + ")";
  }

  private static String toFunctionCallString(final String functionName, final Object parameter1, final Object parameter2) {
    return functionName + "(" + parameter1.toString() + ", " + parameter2.toString() + ")";
  }

  //endregion Static help methods
}