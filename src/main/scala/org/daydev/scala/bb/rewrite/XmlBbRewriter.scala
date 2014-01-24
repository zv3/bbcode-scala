package org.daydev.scala.bb.rewrite

import scala.xml.{Text, NodeSeq}
import org.daydev.scala.bb.model.{BbElement, BbTag, PlainText}
import org.daydev.scala.bb.rewrite.config.RewriteConfig
import org.daydev.scala.bb.parse.BbParser

trait XmlBbRewriter extends BbRewriter[NodeSeq] {
  self: BbParser =>

  def rewriteTag(tag: BbElement)(implicit config: RewriteConfig[NodeSeq]): NodeSeq = tag match {
    case PlainText(text) => Text(text)
    case t@BbTag(name, attr, children) => config(name)(t, children.map(rewriteTag))
  }

}
