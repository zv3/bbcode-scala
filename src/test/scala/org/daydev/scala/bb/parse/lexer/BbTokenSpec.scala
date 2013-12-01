package org.daydev.scala.bb.parse.lexer

import org.scalatest.FunSuite

class BbTokenSpec extends FunSuite {
  
  test("Close tag toString should produce correct bb-string") {
    val tag = CloseTag("foo")
    assert(tag.toString === "[/foo]")
  }
  
  test("Open tag without attr toString produce correct bb-string") {
    val tag = OpenTag("foo", None)
    assert(tag.toString === "[foo]")
  }
  
  test("Open tag with attr toString produce correct bb-string") {
    val tag = OpenTag("foo", Some("attr"))
    assert(tag.toString === "[foo=attr]")
  }

}