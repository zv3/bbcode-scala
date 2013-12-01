package org.daydev.scala.bb.model

import org.scalatest.FunSuite

class BbElementSpec extends FunSuite {

  test("tag with attr to string") {
    val tag = BbTag("url", Some("http://example.com"), "Link")
    assert(tag.toString === "[url=http://example.com]Link[/url]")
  }

  test("tag without attr to string") {
    val tag = BbTag("url", "http://example.com")
    assert(tag.toString === "[url]http://example.com[/url]")
  }

  test("nested tags to string") {
    val tag = BbTag(
      "quote",
      Seq(
        BbTag(
          "b",
          "bold"),
        PlainText(" just text "),
        BbTag(
          "i",
          "italics"
        )))

    assert(tag.toString === "[quote][b]bold[/b] just text [i]italics[/i][/quote]")
  }

  test("plain text addition") {
    assert(PlainText("a") + PlainText("b") === PlainText("ab"))
  }

  test("list of texsts should be correctly reduced") {
    assert(Seq(PlainText("a"), PlainText("B"), PlainText("some")).reduceLeft(_ + _) === PlainText("aBsome"))
  }

}