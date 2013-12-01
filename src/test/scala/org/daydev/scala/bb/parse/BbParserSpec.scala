package org.daydev.scala.bb.parse

import org.scalatest.FunSuite
import org.daydev.scala.bb.model.PlainText
import org.daydev.scala.bb.model.BbTag

trait BbParserSpec extends FunSuite {
  self: BbParser =>

  test("should parse single char") {
    assert(parse("a") === Seq(PlainText("a")))
  }

  test("should rewrite newline as [br]") {
    assert(parse("\n") === Seq(BbTag("br")))
  }

  test("should parse simple tag") {
    assert(parse("[a]text[/a]") === Seq(BbTag("a", "text")))
  }

  test("should parse tag with attr") {
    assert(parse("[a=b]text[/a]") === Seq(BbTag("a", "b", "text")))
  }

  test("should parse nested tags") {
    assert(parse("[a][b][c]text[/c][/b][/a]") === Seq(
      BbTag("a", BbTag("b", BbTag("c", "text")))
    ))
  }

  test("should parse broken tags as text") {
    assert(parse("[a=b]text[/a][b]other[/b][c=d]more") === Seq(
      BbTag("a", "b", "text"),
      BbTag("b", "other"),
      PlainText("[c=d]more")
    ))
  }

  test("should handle overlapping tags strictly") {
    assert(parse("[b][i][c]text[/c][/b][/i]") === Seq(
      BbTag("b", Seq(
        PlainText("[i]"),
        BbTag("c", "text")
      )),
      PlainText("[/i]")
    ))
  }

  test("should merge consequtive texts into single element") {
    assert(parse("text[tag]more[") === Seq(PlainText("text[tag]more[")))
  }

}