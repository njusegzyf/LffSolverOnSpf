package cn.nju.seg.atg.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import cn.nju.seg.atg.parse.CFGBuilder;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.exception.EvalException;
import com.greenpineyu.fel.exception.ParseException;

public final class ComputeEngine {

  /**
   * 简单子约束的运行时刻的时是值 Fel计算引擎
   */
  public static FelEngine myFelEngine = new FelEngineImpl();

  public static FelContext myCtx = myFelEngine.getContext();

  public static ScriptEngineManager manager = new ScriptEngineManager();

  public static ScriptEngine se = manager.getEngineByName("js");

  public static synchronized void initForFel() {
    BasicMathFunc basicMathFunc = new BasicMathFunc();
    myCtx.set("Math", basicMathFunc);
  }

  public static synchronized double getDouForFel(String calaStr, double[] paraForNow) {
    for (int i = 0; i < CFGBuilder.parameterNames.length; i++) {
      myCtx.set(CFGBuilder.parameterNames[i], paraForNow[i]);
    }

    double finalRes = 0;
    if (!calaStr.contains("atan2") && !calaStr.contains("atan")) {
      try {
        Object result = myFelEngine.eval(calaStr);
        finalRes = Double.parseDouble(result.toString());
      } catch (EvalException e) {
        System.err.println(calaStr);
        System.err.println("EvalException !!!!!");
      } catch (ClassCastException e) {
        e.printStackTrace();
        System.err.println(calaStr);
        System.err.println("ClassCastException !!!!!");
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        System.err.println(calaStr);
        System.err.println("IllegalArgumentException: argument type mismatch !!!!!");
      } catch (ParseException e) {
        e.printStackTrace();
        System.err.println(calaStr);
        System.err.println("ParseException !!!!!");
      }
    } else {
      //备用方案
      StringBuilder tmpBuilder = new StringBuilder();
      for (int i = 0; i < CFGBuilder.parameterNames.length; i++) {
        tmpBuilder.append(CFGBuilder.parameterNames[i]);
        tmpBuilder.append("=");
        tmpBuilder.append(paraForNow[i]);
        tmpBuilder.append(";");
      }
      tmpBuilder.append(calaStr);
      try {
        finalRes = (double) se.eval(tmpBuilder.toString());
      } catch (ScriptException e1) {
        e1.printStackTrace();
      }
    }

    return finalRes;
  }

  public static synchronized boolean getBoolForFel(String calaStr, double[] paraForNow) {
    for (int i = 0; i < CFGBuilder.parameterNames.length; i++) {
      myCtx.set(CFGBuilder.parameterNames[i], paraForNow[i]);
    }

    boolean finalRes = false;
    if (!calaStr.contains("atan2") && !calaStr.contains("atan")) {
      try {
        Object result = myFelEngine.eval(calaStr);
        finalRes = Boolean.parseBoolean(result.toString());
      } catch (EvalException e) {
        // TODO: handle exception
        e.printStackTrace();
        System.err.println(calaStr);
        System.err.println("EvalException !!!!!");
      } catch (ClassCastException e) {
        // TODO: handle exception
        e.printStackTrace();
        System.err.println(calaStr);
        System.err.println("ClassCastException !!!!!");
      } catch (IllegalArgumentException e) {
        // TODO: handle exception
        e.printStackTrace();
        System.err.println(calaStr);
        System.err.println("IllegalArgumentException: argument type mismatch !!!!!");
      } catch (ParseException e) {
        // TODO: handle exception
        e.printStackTrace();
        System.err.println(calaStr);
        System.err.println("ParseException !!!!!");
      }
    } else {
      //备用方案
      StringBuilder tmpBuilder = new StringBuilder();
      for (int i = 0; i < CFGBuilder.parameterNames.length; i++) {
        tmpBuilder.append(CFGBuilder.parameterNames[i]);
        tmpBuilder.append("=");
        tmpBuilder.append(paraForNow[i]);
        tmpBuilder.append(";");
      }
      tmpBuilder.append(calaStr);
      try {
        finalRes = (boolean) se.eval(tmpBuilder.toString());
      } catch (ScriptException e1) {
        System.out.println();
        e1.printStackTrace();
      }
    }
    return finalRes;
  }

  //  public static synchronized void printAllParam() {
  //    System.out.println("********** All Param is as Following **********");
  //    for (int i = 0; i < CFGBuilder.parameterNames.length; i++) {
  //      System.out.print(CFGBuilder.parameterNames[i] + " " + myCtx.get(CFGBuilder.parameterNames[i]) + " ");
  //    }
  //    System.out.println();
  //  }
}
