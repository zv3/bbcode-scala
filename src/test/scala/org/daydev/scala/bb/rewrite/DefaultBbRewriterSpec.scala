package org.daydev.scala.bb.rewrite

import org.scalatest.FunSuite
import scala.xml.Node
import scala.xml.Text
import org.daydev.scala.bb.model.BbTag
import scala.xml.NodeSeq
import org.daydev.scala.bb.rewrite.config._

class DefaultBbRewriterSpec extends FunSuite {

  val rewriter = DefaultBbRewriter

  /*test("should use configuration provided") {
    val updatedConfig = defaultConfig.updateRule("b") {
      (tag, content) => <b>{ content }</b>
    }
    assert(rewriter.rewrite("[b]bold[/b]")(updatedConfig) === <b>bold</b>.toString)
  }*/

  test("should rewrite simple tag as specified by configuration") {
    assert(rewriter.rewrite("[b]bold[/b]") === <strong>bold</strong>.toString)
  }

}