package cn.nju.seg.atg.spfwrapper;

import cn.nju.seg.atg.BEParse.BinaryExpressionParse;
import cn.nju.seg.atg.parse.TestBuilder;
import cn.nju.seg.atg.util.ATG;
import cn.nju.seg.atg.util.PCATG;

/**
 * @author Zhang Yifan
 */
final class LffSolverUtils {

  static Boolean solve(final String binaryExpression,
                              final String nodeName,
                              final String typeString,
                              final String nameString) {
    BinaryExpressionParse.setBinaryExpression(binaryExpression, nodeName);
    BinaryExpressionParse.setParameterTypes(typeString);
    BinaryExpressionParse.setParameterNames(nameString);

    TestBuilder.finalParams = new double[ATG.NUM_OF_PARAM];

    // 执行ATG过程, 判断当前路径是否被覆盖：-1:未覆盖；0~+:被覆盖；
    int isCovered = new PCATG().generateTestDataForSolver();

    return Boolean.valueOf(isCovered != -1);
  }

  private LffSolverUtils() { throw new UnsupportedOperationException(); }
}
