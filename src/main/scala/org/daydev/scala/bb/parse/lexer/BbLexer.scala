package org.daydev.scala.bb.parse.lexer

trait BbLexer {

  def tokenize(input: String): Seq[BbToken]
  
}