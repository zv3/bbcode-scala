package org.daydev.scala.bb.rewrite.config

import scala.xml.NodeSeq
import scala.xml.Text
import org.daydev.scala.bb.model.BbTag

case class HtmlRewriteConfig(ruleMap: Map[String, RewriteRule[NodeSeq]]) extends MapRewriteConfig[NodeSeq] {

  def addRule(key: String)(rule: RewriteRule[NodeSeq]): RewriteConfig[NodeSeq] =
    HtmlRewriteConfig(ruleMap + (key -> rule))

  def updateRule(key: String)(rule: RewriteRule[NodeSeq]): RewriteConfig[NodeSeq] =
    HtmlRewriteConfig(ruleMap.updated(key, rule))

  def dropRule(key: String): RewriteConfig[NodeSeq] =
    HtmlRewriteConfig(ruleMap - key)

  def defaultRule: RewriteRule[NodeSeq] = HtmlRewriteConfig.defaultRule

}

object HtmlRewriteConfig {

  lazy val default = new HtmlRewriteConfig(defaultRuleMap)

  val defaultRuleMap = Map[String, RewriteRule[NodeSeq]](
    "b" -> simple("strong"),
    "br" -> brRewrite,
    "code" -> codeRewrite,
    "color" -> styledSpan("color"),
    "font" -> styledSpan("font-family"),
    "i" -> simple("em"),
    "img" -> imgRewrite,
    "li" -> simple("li"),
    "list" -> listRewrite,
    "quote" -> quoteRewrite,
    "s" -> simple("del"),
    "size" -> sizeRewrite,
    "sub" -> simple("sub"),
    "sup" -> simple("sup"),
    "tt" -> ttRewrite,
    "u" -> simple("ins"),
    "url" -> urlRewrite
  ).withDefault(_ => defaultRule _)

  def brRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = <br/>

  def codeRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = <pre>{ tag.children.mkString }</pre>

  def imgRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = tag.attr match {
    case Some(alt) => <img alt={ alt } src={ content.mkString }/>
    case None => <img src={ content.mkString }/>
  }

  def listRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = tag.attr match {
    case Some(t) if "1AaIi".contains(t) => <ol type={ t }>{ content }</ol>
    case Some(t) => <ol>{ content }</ol>
    case None => <ul>{ content }</ul>
  }

  def quoteRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = tag.attr match {
    case Some(source) => <blockquote cite={ source }>{ content }</blockquote>
    case None => <blockquote>{ content }</blockquote>
  }

  def sizeRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = {
    val sizeValue = tag.attr match {
      case Some(x) if (x.forall(_.isDigit)) => x + "px"
      case Some(x) => x
      case None => ""
    }
    <span style={ s"font-size: $sizeValue" }>{ content }</span>
  }

  def ttRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = <span style="font-family: monospace">{ content }</span>

  def urlRewrite(tag: BbTag, content: Seq[NodeSeq]): NodeSeq = tag.attr match {
    case Some(url) => <a href={ url }>{ content }</a>
    case None => <a href={ content.mkString }>{ content }</a>
  }

  def simple(htmlTag: String): RewriteRule[NodeSeq] =
    (tag, content) =>
      <tmp>{ content }</tmp>.copy(label = htmlTag)

  def styledSpan(style: String): RewriteRule[NodeSeq] =
    (tag, content) =>
      <span style={ s"""$style: ${tag.attr.getOrElse("")}""" }>{ content }</span>

  def defaultRule(tag: BbTag, content: Seq[NodeSeq]): NodeSeq =
    Text(s"[${tag.name}${tag.attr.map("=" + _).getOrElse("")}]") ++
      content.foldRight(Text(s"[/${tag.name}]"): NodeSeq)(_ ++ _)
}

