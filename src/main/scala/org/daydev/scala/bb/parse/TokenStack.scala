package org.daydev.scala.bb.parse

import org.daydev.scala.bb.model.BbElement
import org.daydev.scala.bb.parse.lexer.BbToken
import org.daydev.scala.bb.model.PlainText
import org.daydev.scala.bb.parse.lexer.CloseTag
import org.daydev.scala.bb.parse.lexer.OpenTag

case class TokenStack private (stack: Seq[Either[BbToken, BbElement]]) {

  def push(token: BbToken): TokenStack = TokenStack(Left(token) +: stack)

  def push(bb: BbElement): TokenStack = (bb, stack.headOption) match {
    case (PlainText(s1), Some(Right(PlainText(s2)))) => TokenStack(Right(PlainText(s2 + s1)) +: stack.tail)
    case _ => TokenStack(Right(bb) +: stack)
  }

  def pop: TokenStack = TokenStack(stack.tail)

  def splitAt(i: Int): (TokenStack, TokenStack) = {
    val (begin, end) = stack.splitAt(i)
    (TokenStack(begin), TokenStack(end))
  }

  def indexOf(token: BbToken): Int = stack.indexOf(Left(token))

  def indexOf(bb: BbElement): Int = stack.indexOf(Right(bb))

  def findMatch(tag: CloseTag): Option[BbToken] = stack.find {
    case Left(OpenTag(name, _)) if name == tag.name => true
    case _ => false
  }.map(_.left.get)

  lazy val finished: Seq[BbElement] = stack.foldRight(TokenStack()) {
    (elem, stack) =>
      elem match {
        case Left(token) => stack.push(PlainText(token.toString))
        case Right(bb) => stack.push(bb)
      }
  }.stack.reverse.map(_.right.get)

}

object TokenStack {

  def apply(): TokenStack = apply(IndexedSeq())

}
