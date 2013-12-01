package org.daydev.scala.bb.parse

import org.scalatest.FunSuite
import org.daydev.scala.bb.parse.lexer.OpenTag
import org.daydev.scala.bb.parse.lexer.CloseTag
import org.daydev.scala.bb.parse.lexer.Newline
import org.daydev.scala.bb.model.BbTag
import org.daydev.scala.bb.model.PlainText

class TokenStackSpec extends FunSuite {

  test("should be able to push tokens") {
    val stack = TokenStack()
    assert {
      stack.push(OpenTag("a")).push(CloseTag("b")).push(Newline).stack ===
        Seq(
          Left(Newline),
          Left(CloseTag("b")),
          Left(OpenTag("a")))
    }
  }

  test("should be able to push tags") {
    val stack = TokenStack()
    assert {
      stack.push(BbTag("a")).push(BbTag("b", "c")).push(PlainText("text")).stack ===
        Seq(
          Right(PlainText("text")),
          Right(BbTag("b", "c")),
          Right(BbTag("a")))
    }
  }

  test("should be able to interleave tokens and tags") {
    val stack = TokenStack()
    assert {
      stack.push(OpenTag("a")).push(BbTag("b", "c")).push(PlainText("text")).push(Newline).stack ===
        Seq(
          Left(Newline),
          Right(PlainText("text")),
          Right(BbTag("b", "c")),
          Left(OpenTag("a")))
    }
  }

  test("should merge consequitive texts") {
    val stack = TokenStack()
    assert {
      stack.push(PlainText("a")).push(PlainText("b")).push(PlainText("c")).stack ===
        Seq(Right(PlainText("abc")))
    }
  }

  test("shold be able to produce finished sequence") {
    val stack = TokenStack()
    assert {
      stack.push(OpenTag("a")).push(BbTag("b", "c")).push(PlainText("text")).push(Newline).finished ===
        Seq(
          PlainText("[a]"),
          BbTag("b", "c"),
          PlainText("text\n"))
    }
  }

  test("should find matching open tag") {
    val stack = TokenStack().push(OpenTag("a", Some("b"))).push(BbTag("b", "c")).push(PlainText("text")).push(Newline)
    assert(stack.findMatch(CloseTag("a")) === Some(OpenTag("a", Some("b"))))
  }

}