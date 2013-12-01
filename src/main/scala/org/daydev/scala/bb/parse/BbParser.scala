package org.daydev.scala.bb.parse

import org.daydev.scala.bb.model.BbElement

trait BbParser {

  def parse(document: String): Seq[BbElement]

}