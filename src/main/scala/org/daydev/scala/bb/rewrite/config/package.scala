package org.daydev.scala.bb.rewrite

import org.daydev.scala.bb.model.BbElement
import scala.xml.Node
import org.daydev.scala.bb.model.BbTag
import scala.xml.NodeSeq

package object config {

  type RewriteRule[A] = (BbTag, Seq[A]) => A

  implicit val defaultConfig = HtmlRewriteConfig.default

}