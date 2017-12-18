package cn.nju.seg.zhangyf.lff

import scala.compat.Platform

import javax.annotation.{ CheckReturnValue, Nonnull, ParametersAreNonnullByDefault }

/**
  * @author Zhang Yifan
  */
@ParametersAreNonnullByDefault
private[lff] object InputUtils {

  @Nonnull @CheckReturnValue
  def convertToInputConfStringLines(inputs: Iterable[Array[Double]],
                                    argFixer: Double => Double = { d => d })
  : String = {
    assert(inputs != null)

    val inputsContent: String = inputs.view
                                .map { args => s"    { Input: [ ${ args map argFixer mkString "," } ] }," }
                                .mkString(Platform.EOL)

    raw"""{
         |  Inputs: [
         |$inputsContent
         |  ]
         |}
         |""".stripMargin
  }

  private val MAX_ERROR: Double = 1E-5

  val defaultArgFixer: Double => Double = { arg =>
    val roundedArg: Double = Math.round(arg).toDouble
    if (Math.abs(roundedArg - arg) > MAX_ERROR) arg
    else roundedArg
  }
}
