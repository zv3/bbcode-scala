package org.daydev.scala.bb.parse.lexer

import scala.language.postfixOps
import scala.util.parsing.combinator.RegexParsers

trait GrammarCombinatorsBbLexer extends BbLexer {

  def tokenize(input: String): Seq[BbToken] = BbGrammar.parse(BbGrammar.document, input).getOrElse(Nil)

  private object BbGrammar extends RegexParsers {

    override def skipWhitespace = false

    def document = (closeTag | openTag | newline | text)*

    def openTag = "[" ~> tagName ~ opt("=" ~> attrValue) <~ "]" ^^ {
      case tag ~ attr => OpenTag(tag.toLowerCase, attr)
    }

    def closeTag = "[" ~> "/" ~> tagName <~ "]" ^^ {
      case tag => CloseTag(tag.toLowerCase)
    }

    def text = ("""[^\[\n\r]+""".r | "[") ^^ { Text(_) }

    def newline = """\r?\n""".r ^^^ Newline

    def tagName = """[\w\d_-]+""".r

    def attrValue = "'" ~> """[^']+""".r <~ "'" |
      "\"" ~> """[^"]+""".r <~ "\"" |
      """[^\]]+""".r

  }

}