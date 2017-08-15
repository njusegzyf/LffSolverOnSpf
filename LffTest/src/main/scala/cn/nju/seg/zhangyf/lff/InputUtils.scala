package cn.nju.seg.zhangyf.lff

import scala.compat.Platform

import javax.annotation.{ CheckReturnValue, Nonnull, ParametersAreNonnullByDefault }

/**
  * @author Zhang Yifan
  */
@ParametersAreNonnullByDefault
object InputUtils {

  @Nonnull @CheckReturnValue
  def convertToInputConfStringLines(inputs: Iterable[Array[Double]]): String = {
    assert(inputs != null)

    val inputsContent: String = inputs.view
                                .map { args => s"    { Input: [ ${ args.mkString(",") } ] }," }
                                .mkString(Platform.EOL)

    raw"""{
         |  Inputs: [
         |$inputsContent
         |  ]
         |}
         |""".stripMargin
  }
}
