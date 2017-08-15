package cn.nju.seg.zhangyf.lfftest

import javax.annotation.{ Nonnull, ParametersAreNonnullByDefault }
import java.lang.Math._

import cn.nju.seg.zhangyf.lff.Tsafes

/**
  * @author Zhang Yifan
  */
@ParametersAreNonnullByDefault
final class TsafeTest {

  //region Conflict

  @Nonnull
  private def testConflict(name: String)
                          (pathCondition: Array[Double] => Boolean)
  : Option[Array[Double]] = {
    val solveRes: Option[Array[Double]] = Tsafes.cwConflictTestCases.find { pathCondition }
    if (solveRes.isDefined) {
      println(s"CW solve Tsafe Conflict $name with : ${ solveRes.get.mkString(",") }")
    } else {
      println(s"CW can not solve Tsafe Conflict $name.")
    }
    solveRes
  }

  def testConflict01(): Unit = {
    val solveRes: Option[Array[Double]] =
      this.testConflict("01") { startPoint =>
        val Array(psi1_1_SYMREAL, vA_2_SYMREAL, vC_3_SYMREAL, xC0_4_SYMREAL, yC0_5_SYMREAL, psiC_6_SYMREAL, bank_ang_7_SYMREAL) = startPoint
        //region  @formatter:off
        exp(pow(xC0_4_SYMREAL + ((1.0 * ((pow(vC_3_SYMREAL, 2.0) / tan(0.017453292519943295 * bank_ang_7_SYMREAL)) / 68443.0)) * (cos(psiC_6_SYMREAL) - cos(psiC_6_SYMREAL + (((1.0 * (((0.0 - (0.017453292519943295 * psi1_1_SYMREAL)) * ((pow(vA_2_SYMREAL, 2.0) / tan(0.017453292519943295 * bank_ang_7_SYMREAL)) / 68443.0)) / vA_2_SYMREAL)) * vC_3_SYMREAL) / ((pow(vC_3_SYMREAL, 2.0) / tan(0.017453292519943295 * bank_ang_7_SYMREAL)) / 68443.0))))) - ((-1.0 * ((pow(vA_2_SYMREAL, 2.0) / tan(0.017453292519943295 * bank_ang_7_SYMREAL)) / 68443.0)) * (1.0 - cos(0.017453292519943295 * psi1_1_SYMREAL))), 2.0) + pow((yC0_5_SYMREAL - ((1.0 * ((pow(vC_3_SYMREAL, 2.0) / tan(0.017453292519943295 * bank_ang_7_SYMREAL)) / 68443.0)) * (sin(psiC_6_SYMREAL) - sin(psiC_6_SYMREAL + (((1.0 * (((0.0 - (0.017453292519943295 * psi1_1_SYMREAL)) * ((pow(vA_2_SYMREAL, 2.0) / tan((0.017453292519943295 * bank_ang_7_SYMREAL))) / 68443.0)) / vA_2_SYMREAL)) * vC_3_SYMREAL) / ((pow(vC_3_SYMREAL, 2.0) / tan( 0.017453292519943295 * bank_ang_7_SYMREAL )) / 68443.0)))))) - ((-1.0 * ((pow(vA_2_SYMREAL, 2.0) / tan(0.017453292519943295 * bank_ang_7_SYMREAL)) / 68443.0)) * sin(0.017453292519943295 * psi1_1_SYMREAL)), 2.0)) < 999.0 && ((pow(vC_3_SYMREAL, 2.0) / tan(0.017453292519943295 * bank_ang_7_SYMREAL)) / 68443.0) != 0.0 && vA_2_SYMREAL != 0.0 && tan(0.017453292519943295 * bank_ang_7_SYMREAL) != 0.0 && tan(0.017453292519943295 * bank_ang_7_SYMREAL) != 0.0 && (0.017453292519943295 * psi1_1_SYMREAL) < 0.0
        //endregion  @formatter:on
      }
  }

  //endregion Conflict

  //region 

  @Nonnull
  private def testTurnLogic(name: String)
                           (pathCondition: Array[Double] => Boolean)
  : Option[Array[Double]] = {
    val solveRes: Option[Array[Double]] = Tsafes.cwTurnLogicTestCases.find { pathCondition }
    if (solveRes.isDefined) {
      println(s"CW solve Tsafe Conflict $name with : ${ solveRes.get.mkString(",") }")
    } else {
      println(s"CW can not solve Tsafe Conflict $name.")
    }
    solveRes
  }

  //endregion

  @org.testng.annotations.BeforeClass
  def setUp(): Unit = {}

  @org.testng.annotations.AfterClass
  def tearDown(): Unit = {}
}
