package testForAll;

import java.util.List;

import cn.nju.seg.atg.BEParse.BinaryExpressionParse;

public class TestForAll {

  public static void main(String[] args) {
    BenchMark.initBenchmark();
    List<String> functionName = BenchMark.functionName;
    List<String> binaryExp = BenchMark.binaryExp;
    List<String> paraName = BenchMark.paraName;
    List<String> paraType = BenchMark.paraType;

    for (int i = 0; i < functionName.size(); i++) {
      BinaryExpressionParse.initAll(binaryExp.get(i), functionName.get(i), paraType.get(i), paraName.get(i));
      BinaryExpressionParse.beginSolver();
      System.gc();
    }
    System.out.println("***** Game Over *****");
  }

}
