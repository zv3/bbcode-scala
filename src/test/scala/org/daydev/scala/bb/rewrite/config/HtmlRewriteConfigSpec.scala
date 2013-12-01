package org.daydev.scala.bb.rewrite.config

import org.scalatest.FunSuite
import org.daydev.scala.bb.model.BbTag
import scala.xml.Text
import scala.xml.NodeSeq

class HtmlRewriteConfigSpec extends FunSuite {

  val dummyRuleMap = Map[String, RewriteRule[NodeSeq]](
    "a" -> {
      (_, content) => <a>{ content }</a>
    },
    "b" -> {
      (_, content) => <b>{ content }</b>
    }
  )

  val config = HtmlRewriteConfig(dummyRuleMap)

  test("html rewrite config should rewrite unknown tag in it's original form") {
    val tag = BbTag("a", "text")
    val tagA = BbTag("a", "b", "text")
    assert(config.defaultRule(tag, Seq(Text("text"))).mkString === "[a]text[/a]")
    assert(config.defaultRule(tagA, Seq(Text("text"))).mkString === "[a=b]text[/a]")
  }

  test("html rewrite config should be exstensible") {
    val html = <c>text</c>
    val extendedConfig = config.addRule("c")((_, _) => html)
    assert(extendedConfig("c")(BbTag("c"), Nil).mkString === html.toString)
  }

  test("html rewrite config should be editable") {
    val html = <c>text</c>
    val updatedConfig = config.updateRule("a")((_, _) => <c>text</c>)
    assert(updatedConfig("a")(BbTag("a"), Nil).mkString === html.toString)
  }

  test("html rewrite config should be able to drop rule") {
    val restrictedConfig = config.dropRule("a")
    assert(restrictedConfig("a")(BbTag("a"), Seq(Text("text"))).mkString === "[a]text[/a]")
  }

}