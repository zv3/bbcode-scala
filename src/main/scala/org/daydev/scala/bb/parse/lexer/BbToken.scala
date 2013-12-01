package org.daydev.scala.bb.parse.lexer

sealed trait BbToken

case class OpenTag(name: String, attr: Option[String] = None) extends BbToken {
  override lazy val toString = s"[$name${attr.map("=" + _).getOrElse("")}]"
}

case class CloseTag(name: String) extends BbToken {
  override lazy val toString = s"[/$name]"
}

case object Newline extends BbToken {
  override val toString = "\n"
}

case class Text(content: String) extends BbToken {
  override val toString = content
}