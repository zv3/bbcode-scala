package org.daydev.scala.bb.parse.lexer

import org.scalatest.FunSuite

trait BbLexerSpec extends FunSuite {
    self: BbLexer =>
  

  test("should recognize single char as text") {
    assert(tokenize("a") === Seq(Text("a")))
  }

  test("should recognize '[' as text") {
    assert(tokenize("[") === Seq(Text("[")))
  }

  test("should recognize ']' as text") {
    assert(tokenize("]") === Seq(Text("]")))
  }

  test("should recognize string as text") {
    assert(tokenize("abc222d+?111") === Seq(Text("abc222d+?111")))
  }

  test("""should recognize '\n' as newline""") {
    assert(tokenize("""
        |""".stripMargin) === Seq(Newline))
  }

  test("""should recognize multiple '\n' as several newlines""") {
    assert(tokenize("\n\n\n") === Seq.fill(3)(Newline))
  }

  test("should recognize open tag") {
    assert(tokenize("[tag]") === Seq(OpenTag("tag")))
  }

  test("should recognize open tag with attr") {
    assert(tokenize("[tag=attr]") === Seq(OpenTag("tag", Some("attr"))))
  }

  test("should reconize close tag") {
    assert(tokenize("[/tag]") === Seq(CloseTag("tag")))
  }

  test("should not fail on empty string") {
    assert(tokenize("") === Seq())
  }

  test("should correctly process crazy string") {
    assert(tokenize("abc[][oLo=jjj]jjj]]/[/clomp]lol") === Seq(
      Text("abc"),
      Text("["),
      Text("]"),
      OpenTag("olo", Some("jjj")),
      Text("jjj]]/"),
      CloseTag("clomp"),
      Text("lol")
    ))
  }

  test("should correctly process intewoven tags") {
    assert(tokenize("""[a=bbb]
        |[bB]text[/A]some[cCc]more[/bb][/Ccc] and""".stripMargin) === Seq(
      OpenTag("a", Some("bbb")),
      Newline,
      OpenTag("bb"),
      Text("text"),
      CloseTag("a"),
      Text("some"),
      OpenTag("ccc"),
      Text("more"),
      CloseTag("bb"),
      CloseTag("ccc"),
      Text(" and")
    ))
  }

  test("open tag names should be case insensitive") {
    assert(tokenize("[tag]") === tokenize("[Tag]"))
  }

  test("close tag names should be case insensitive") {
    assert(tokenize("[/tag]") === tokenize("[/Tag]"))
  }

  test("should strip single quotes around attr value") {
    assert(tokenize("[tag='attr']") === Seq(OpenTag("tag", Some("attr"))))
  }

  test("should correctly handle unclosed single quote in attr value") {
    assert(tokenize("[tag='attr]") === Seq(OpenTag("tag", Some("'attr"))))
  }

  test("should correctly handle unopened single quote in attr value") {
    assert(tokenize("[tag=attr']") === Seq(OpenTag("tag", Some("attr'"))))
  }

  test("should strip double quotes around attr value") {
    assert(tokenize("""[tag="attr"]""") === Seq(OpenTag("tag", Some("attr"))))
  }

  test("should correctly handle unclosed double quote in attr value") {
    assert(tokenize("[tag=\"attr]") === Seq(OpenTag("tag", Some("\"attr"))))
  }

  test("should correctly handle unopened double quote in attr value") {
    assert(tokenize("[tag=attr\"]") === Seq(OpenTag("tag", Some("attr\""))))
  }

  test("should correctly hable unmatched quotes around attr value") {
    assert(tokenize("[tag=\"attr']") === Seq(OpenTag("tag", Some("\"attr'"))))
  }

}