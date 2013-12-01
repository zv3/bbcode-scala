package org.daydev.scala.bb.rewrite

import org.daydev.scala.bb.parse.BbParser
import org.daydev.scala.bb.rewrite.config.RewriteConfig
import org.daydev.scala.bb.model.BbElement

trait BbRewriter[A] {
  self: BbParser =>

  def rewrite(input: String)(implicit config: RewriteConfig[A]): String = parse(input).map(rewriteTag).mkString

  protected def rewriteTag(tag: BbElement)(implicit config: RewriteConfig[A]): A

}