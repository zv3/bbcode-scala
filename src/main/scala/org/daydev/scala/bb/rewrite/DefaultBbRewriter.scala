package org.daydev.scala.bb.rewrite

import scala.xml.Node
import scala.xml.Text
import org.daydev.scala.bb.parse.TokenStackBbParser
import org.daydev.scala.bb.model.BbElement
import org.daydev.scala.bb.model.PlainText
import org.daydev.scala.bb.model.BbTag
import org.daydev.scala.bb.parse.TokenStackBbParser
import scala.xml.NodeSeq
import org.daydev.scala.bb.rewrite.config.RewriteConfig

object DefaultBbRewriter extends BbRewriter[NodeSeq] with TokenStackBbParser {

  def rewriteTag(tag: BbElement)(implicit config: RewriteConfig[NodeSeq]): NodeSeq = tag match {
    case PlainText(text) => Text(text)
    case t @ BbTag(name, attr, children) => config(name)(t, children.map(rewriteTag))
  }

}