package org.daydev.scala.bb.parse

import scala.collection.Seq
import org.daydev.scala.bb.model.BbElement
import org.daydev.scala.bb.parse.lexer.RegexBbLexer
import org.daydev.scala.bb.parse.lexer.BbToken
import org.daydev.scala.bb.parse.lexer.Text
import org.daydev.scala.bb.model.PlainText
import org.daydev.scala.bb.parse.lexer.Newline
import org.daydev.scala.bb.model.BbTag
import org.daydev.scala.bb.parse.lexer.OpenTag
import org.daydev.scala.bb.parse.lexer.CloseTag
import scala.annotation.tailrec
import org.daydev.scala.bb.model.PlainText
import org.daydev.scala.bb.parse.lexer.OpenTag
import org.daydev.scala.bb.parse.lexer.RegexBbLexer

trait TokenStackBbParser extends BbParser with RegexBbLexer {

  def parse(document: String): Seq[BbElement] = {
    @tailrec
    def buildAst(tokens: Seq[BbToken], stack: TokenStack): Seq[BbElement] = tokens match {
      case Nil => stack.finished
      case head +: rest => head match {
        case Text(s) => buildAst(rest, stack.push(PlainText(s)))
        case Newline => buildAst(rest, stack.push(BbTag("br")))
        case token: OpenTag => buildAst(rest, stack.push(token))
        case token @ CloseTag(name) => {
          stack.findMatch(token) match {
            case Some(matching @ OpenTag(name, attr)) => {
              val (children, restStack) = stack.splitAt(stack.indexOf(matching))
              buildAst(rest, restStack.pop.push(BbTag(name, attr, children.finished)))
            }
            case _ => buildAst(rest, stack.push(PlainText(token.toString)))
          }
        }
      }
    }

    buildAst(tokenize(document), TokenStack())
  }

}