package org.daydev.scala.bb.rewrite

import org.daydev.scala.bb.parse.TokenStackBbParser
import org.daydev.scala.bb.parse.lexer.GrammarCombinatorsBbLexer

object DefaultBbRewriter
  extends XmlBbRewriter
  with TokenStackBbParser
  with GrammarCombinatorsBbLexer