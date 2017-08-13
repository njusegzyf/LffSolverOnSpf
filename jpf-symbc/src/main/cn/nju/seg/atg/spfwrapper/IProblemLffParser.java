package cn.nju.seg.atg.spfwrapper;

import gov.nasa.jpf.symbc.numeric.solvers.ProblemGeneral;

/**
 * @author Zhang Yifan
 * @implNote This class could be an interface with default methods in Java 8.
 */
public abstract class IProblemLffParser extends ProblemGeneral {

  //region eq

  @Override
  public Object eq(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "==", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object eq(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "==", String.valueOf(value));
  }

  @Override
  public Object eq(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "==", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object eq(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "==", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object eq(final Object exp, final double value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "==", String.valueOf(value));
  }

  //endregion eq

  //region neq

  @Override
  public Object neq(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "!=", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object neq(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "!=", String.valueOf(value));
  }

  @Override
  public Object neq(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "!=", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object neq(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "!=", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object neq(final Object exp, final double value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "!=", String.valueOf(value));
  }

  //endregion neq

  //region leq

  @Override
  public Object leq(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "<=", IProblemLffParser.castToExpInstance(exp));
  }

  @Override public Object leq(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "<=", String.valueOf(value));
  }

  @Override
  public Object leq(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "<=", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object leq(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "<=", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object leq(final Object exp, final double value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "<=", String.valueOf(value));
  }

  //endregion leq

  //region geq

  @Override
  public Object geq(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), ">=", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object geq(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), ">=", String.valueOf(value));
  }

  @Override
  public Object geq(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), ">=", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object geq(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), ">=", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object geq(final Object exp, final double value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), ">=", String.valueOf(value));
  }

  //endregion geq

  //region lt

  @Override
  public Object lt(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "<", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object lt(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "<", String.valueOf(value));
  }

  @Override
  public Object lt(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "<", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object lt(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "<", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object lt(final Object exp, final double value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "<", String.valueOf(value));
  }

  //endregion lt

  //region gt

  @Override
  public Object gt(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), ">", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object gt(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), ">", String.valueOf(value));
  }

  @Override
  public Object gt(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), ">", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object gt(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), ">", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object gt(final Object exp, final double value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), ">", String.valueOf(value));
  }

  //endregion gt

  //region plus

  @Override
  public Object plus(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "+", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object plus(final Object exp, final int value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "+", String.valueOf(value));
  }

  @Override
  public Object plus(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "+", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object plus(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "+", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object plus(final Object exp, final double value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "+", String.valueOf(value));
  }

  //endregion plus

  //region minus

  @Override
  public Object minus(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "-", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object minus(final Object exp, final int value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "-", String.valueOf(value));
  }

  @Override
  public Object minus(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(String.valueOf(exp1), "-", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object minus(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "-", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object minus(final Object exp, final double value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "-", String.valueOf(value));
  }

  //endregion minus

  //region mult

  @Override
  public Object mult(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "*", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object mult(final Object exp, final int value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "*", String.valueOf(value));
  }

  @Override
  public Object mult(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(String.valueOf(exp1), "*", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object mult(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "*", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object mult(final Object exp, final double value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "*", String.valueOf(value));
  }

  //endregion mult

  //region div

  @Override
  public Object div(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "/", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object div(final Object exp, final int value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "/", String.valueOf(value));
  }

  @Override
  public Object div(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(String.valueOf(exp1), "/", IProblemLffParser.castToExpInstance(exp2));
  }

  @Override
  public Object div(final double value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "/", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object div(final Object exp, final double value) {
    return IProblemLffParser.toExprString(String.valueOf(exp), "/", String.valueOf(value));
  }

  //endregion div

  //region Math operations

  @Override
  public Object sin(Object exp) {
    return IProblemLffParser.toFunctionCallString("sin", exp);
  }

  @Override
  public Object cos(Object exp) {
    return IProblemLffParser.toFunctionCallString("cos", exp);
  }

  @Override
  public Object round(Object exp) {
    return IProblemLffParser.toFunctionCallString("round", exp);
  }

  @Override
  public Object exp(Object exp) {
    return IProblemLffParser.toFunctionCallString("exp", exp);
  }

  @Override
  public Object asin(Object exp) {
    return IProblemLffParser.toFunctionCallString("asin", exp);
  }

  @Override
  public Object acos(Object exp) {
    return IProblemLffParser.toFunctionCallString("acos", exp);
  }

  @Override
  public Object atan(Object exp) {
    return IProblemLffParser.toFunctionCallString("atan", exp);
  }

  @Override
  public Object log(Object exp) {
    return IProblemLffParser.toFunctionCallString("log", exp);
  }

  @Override
  public Object tan(Object exp) {
    return IProblemLffParser.toFunctionCallString("tan", exp);
  }

  @Override
  public Object sqrt(Object exp) {
    return IProblemLffParser.toFunctionCallString("sqrt", exp);
  }

  @Override
  public Object power(Object exp1, Object exp2) {
    return IProblemLffParser.toFunctionCallString("pow", exp1, exp2);
  }

  @Override
  public Object power(Object exp1, double exp2) {
    return IProblemLffParser.toFunctionCallString("pow", exp1, exp2);
  }

  @Override
  public Object power(double exp1, Object exp2) {
    return IProblemLffParser.toFunctionCallString("pow", exp1, exp2);
  }

  @Override
  public Object atan2(Object exp1, Object exp2) {
    return IProblemLffParser.toFunctionCallString("atan2", exp1, exp2);
  }

  @Override
  public Object atan2(Object exp1, double exp2) {
    return IProblemLffParser.toFunctionCallString("atan2", exp1, exp2);
  }

  @Override
  public Object atan2(double exp1, Object exp2) {
    return IProblemLffParser.toFunctionCallString("atan2", exp1, exp2);
  }

  //endregion Math operations

  //region bitwise and

  @Override
  public Object and(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "&", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object and(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "&", String.valueOf(value));
  }

  @Override
  public Object and(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "&", IProblemLffParser.castToExpInstance(exp2));
  }

  //endregion bitwise and

  //region bitwise or

  @Override
  public Object or(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "|", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object or(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "|", String.valueOf(value));
  }

  @Override
  public Object or(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "|", IProblemLffParser.castToExpInstance(exp2));
  }

  //endregion bitwise or

  //region bitwise xor

  @Override
  public Object xor(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "^", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object xor(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "^", String.valueOf(value));
  }

  @Override
  public Object xor(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "^", IProblemLffParser.castToExpInstance(exp2));
  }

  //endregion bitwise xor

  //region shift left

  @Override
  public Object shiftL(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), "<<", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object shiftL(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), "<<", String.valueOf(value));
  }

  @Override
  public Object shiftL(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "<<", IProblemLffParser.castToExpInstance(exp2));
  }

  //endregion shift left

  //region singed shift right

  @Override
  public Object shiftR(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), ">>", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object shiftR(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), ">>", String.valueOf(value));
  }

  @Override
  public Object shiftR(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), ">>", IProblemLffParser.castToExpInstance(exp2));
  }

  //endregion singed shift right

  //region unsigned shift right

  @Override
  public Object shiftUR(final int value, final Object exp) {
    return IProblemLffParser.toExprString(String.valueOf(value), ">>>", IProblemLffParser.castToExpInstance(exp));
  }

  @Override
  public Object shiftUR(final Object exp, final int value) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp), ">>>", String.valueOf(value));
  }

  @Override
  public Object shiftUR(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), ">>>", IProblemLffParser.castToExpInstance(exp2));
  }

  //endregion unsigned shift right

  @Override
  public Object mixed(final Object exp1, final Object exp2) {
    return IProblemLffParser.toExprString(IProblemLffParser.castToExpInstance(exp1), "=", IProblemLffParser.castToExpInstance(exp2));// refer to ProblemIAsolver
  }

  //region Static help methods

  protected static String castToExpInstance(final Object exp) {
    return (String) exp;
  }

  protected static String toExprString(final String left, final String op, final String right) {
    return "(" + left + op + right + ")";
  }

  protected static String toFunctionCallString(final String functionName, final Object parameter) {
    return functionName + "(" + parameter.toString() + ")";
  }

  protected static String toFunctionCallString(final String functionName, final Object parameter1, final Object parameter2) {
    return functionName + "(" + parameter1.toString() + ", " + parameter2.toString() + ")";
  }

  //endregion Static help methods
}
