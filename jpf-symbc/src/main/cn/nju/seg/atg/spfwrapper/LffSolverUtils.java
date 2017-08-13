package cn.nju.seg.atg.spfwrapper;

import com.google.common.base.Strings;

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
    assert !Strings.isNullOrEmpty(binaryExpression);
    assert !Strings.isNullOrEmpty(nodeName);
    assert !Strings.isNullOrEmpty(typeString);
    assert !Strings.isNullOrEmpty(nameString);

    BinaryExpressionParse.setBinaryExpression(binaryExpression, nodeName);
    BinaryExpressionParse.setParameterTypes(typeString);
    BinaryExpressionParse.setParameterNames(nameString);

    TestBuilder.finalParams = new double[ATG.NUM_OF_PARAM];

    // 执行ATG过程, 判断当前路径是否被覆盖：-1:未覆盖；0~+:被覆盖；
    final int isCovered = new PCATG().generateTestDataForSolver();

    return Boolean.valueOf(isCovered != -1);
  }

  static void setStartPoint(final double[] startPoint) {
    assert startPoint != null && startPoint.length != 0;

    ATG.CUSTOMIZED_PARAMS = startPoint;
  }

  static void clearStartPoint() {
    ATG.CUSTOMIZED_PARAMS = LffSolverUtils.DEFAULT_START_POINT;
  }

  private static final double[] DEFAULT_START_POINT = {};

  @Deprecated
  private LffSolverUtils() { throw new UnsupportedOperationException(); }
}
