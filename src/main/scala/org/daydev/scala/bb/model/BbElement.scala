package org.daydev.scala.bb.model

import scala.language.implicitConversions

sealed trait BbElement

case class PlainText(content: String) extends BbElement {

  def +(other: PlainText): PlainText = PlainText(content + other.content)

  override val toString = content

}

case class BbTag(name: String, attr: Option[String], children: Seq[BbElement]) extends BbElement {

  override lazy val toString = s"[$name${attr.map("=" + _).getOrElse("")}]${children.mkString}[/$name]"

}

object BbTag {

  def apply(name: String): BbTag = apply(name, None, Nil)

  def apply(name: String, attr: String, children: Seq[BbElement]): BbTag = apply(name, Some(attr), children)

  def apply(name: String, children: Seq[BbElement]): BbTag = apply(name, None, children)

  def apply(name: String, attr: Option[String], child: BbElement): BbTag = apply(name, attr, Seq(child))

  def apply(name: String, attr: String, child: BbElement): BbTag = apply(name, Some(attr), Seq(child))

  def apply(name: String, child: BbElement): BbTag = apply(name, None, Seq(child))

  def apply(name: String, attr: Option[String], content: String): BbTag = apply(name, attr, Seq(PlainText(content)))

  def apply(name: String, attr: String, content: String): BbTag = apply(name, Some(attr), Seq(PlainText(content)))

  def apply(name: String, content: String): BbTag = apply(name, None, Seq(PlainText(content)))

}