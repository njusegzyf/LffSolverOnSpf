package cn.nju.seg.zhangyf.lfftest

import org.testng.Assert
import org.testng.annotations.Test
import java.text.DecimalFormat

/**
  * @author Zhang Yifan
  */
final class OptiTest {

  @Test
  def testFreudensteinRoth(): Unit = {
    Assert.assertFalse(this.freudensteinRoth(0.08556294559697797, 4.315698351691307))
    Assert.assertFalse(this.freudensteinRoth(2.0999455945529, 3.070225600011028))
    // LFF's solution
    Assert.assertTrue(this.freudensteinRoth(23.311078148340442, 2.337035677157253))

    // CW's solution
    Assert.assertTrue(this.freudensteinRoth(21.0, 0.0))
  }

  @Test
  def testHelicalValley(): Unit = {
    // CW's solution
    Assert.assertTrue(this.helicalValley2(1.0, 0.0, 0.0))
  }

  //  @Test
  //  def test(): Unit = {
  //    val Array(x1_1_SYMREAL, x2_2_SYMREAL, x3_3_SYMREAL) = Array(1.0, 0.0, 0.0)
  //    val x1_4_SYMREAL = x1_1_SYMREAL
  //    val x2_5_SYMREAL = x2_2_SYMREAL
  //    val res = (0.0 == (10.0 * (sqrt((x1_1_SYMREAL * x1_1_SYMREAL) + (x2_2_SYMREAL * x2_2_SYMREAL)) - 1.0))
  //               && 0.0 == (10.0 * (x3_3_SYMREAL - (10.0 * (atan(x2_5_SYMREAL / x1_4_SYMREAL) / 6.283185307179586))))
  //               && x1_4_SYMREAL != 0.0
  //               && x1_4_SYMREAL > 0.0
  //              )
  //   Assert.assertTrue(res)
  //  }

  //  def freudensteinRoth(x1: Double, x2: Double): Unit = {
  //    if ((-13.0 + x1 + ((5.0 - x2) * x2 - 2.0) * x2) + (-29.0 + x1 + ((x2 + 1.0) * x2 - 14.0) * x2) == 0.0) {
  //      val fnum = new DecimalFormat("#0.0000000000000000000000000000000000000000")
  //      val d = fnum.format((-13.0 + x1 + ((5.0 - x2) * x2 - 2.0) * x2) + (-29.0 + x1 + ((x2 + 1.0) * x2 - 14.0) * x2))
  //      System.out.println(d)
  //      System.out.println("Solved Freudenstein and Roth constraint")
  //    }
  //  }

  def freudensteinRoth(x1: Double, x2: Double): Boolean =
    (-13.0 + x1 + ((5.0 - x2) * x2 - 2.0) * x2) + (-29.0 + x1 + ((x2 + 1.0) * x2 - 14.0) * x2) == 0.0

  def freudensteinRothError(x1: Double, x2: Double): Double =
    (-13.0 + x1 + ((5.0 - x2) * x2 - 2.0) * x2) + (-29.0 + x1 + ((x2 + 1.0) * x2 - 14.0) * x2)

  def beale(x1: Double, x2: Double): Unit = {
    if ((1.5 - x1 * (1.0 - x2)) == 0.0) System.out.println("Solved Beale constraint")
  }

  // This is public only because JPF keeps generating test cases for it and it is highly annoying to remove them every time we regenerate them.
  def theta(x1: Double, x2: Double): Double = {
    if (x1 > 0.0) return Math.atan(x2 / x1) / (2 * Math.PI)
    else if (x1 < 0.0) return Math.atan(x2 / x1) / (2 * Math.PI) + 0.5
    0.0
  }

  def helicalValley(x1: Double, x2: Double, x3: Double): Unit = {
    if (10.0 * (x3 - 10.0 * theta(x1, x2)) == 0 && (10.0 * (Math.sqrt(x1 * x1 + x2 * x2) - 1)) == 0.0 && x3 == 0.0) {
      val fnum = new DecimalFormat("#0.0000000000000000000000000000000000000000")
      val d1 = fnum.format(10.0 * (x3 - 10.0 * theta(x1, x2)))
      val d2 = fnum.format(10.0 * (Math.sqrt(x1 * x1 + x2 * x2) - 1))
      System.out.println(d1)
      System.out.println(d2)
      System.out.println("Solved Helical Valley constraint")
    }
  }

  def helicalValley2(x1: Double, x2: Double, x3: Double): Boolean =
    10.0 * (x3 - 10.0 * theta(x1, x2)) == 0 && (10.0 * (Math.sqrt(x1 * x1 + x2 * x2) - 1)) == 0.0 && x3 == 0.0

  def powell(x1: Double, x2: Double): Unit = {
    if ((Math.pow(10, 4) * x1 * x2 - 1.0) == 0.0 && (Math.pow(Math.E, -x1) + Math.pow(Math.E, -x2) - 1.0001) == 0.0) {
      System.out.println("Solved Powell constraint")
    }
  }

  def rosenbrock(x1: Double, x2: Double): Unit = {
    if (Math.pow(1.0 - x1, 2) + 100.0 * Math.pow(x2 - x1 * x1, 2) == 0.0) {
      System.out.println("Solved Rosenbrock consraint")
    }
  }

  def wood(x1: Double, x2: Double, x3: Double, x4: Double): Unit = {
    if ((10.0 * (x2 - x1 * x1)) == 0.0
        && (1.0 - x1) == 0.0
        && (Math.sqrt(90) * (x4 - x3 * x3)) == 0.0
        && (1.0 - x3) == 0.0 && (Math.sqrt(10) * (x2 + x4 - 2.0)) == 0.0
        && (Math.pow(10, -0.5) * (x2 - x4)) == 0.0) {
      System.out.println("Solved Wood constraint")
    }
  }

  @org.testng.annotations.BeforeClass
  def setUp(): Unit = {}

  @org.testng.annotations.AfterClass
  def tearDown(): Unit = {}
}
