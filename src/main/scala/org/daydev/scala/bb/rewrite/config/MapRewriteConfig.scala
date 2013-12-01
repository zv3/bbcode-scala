package org.daydev.scala.bb.rewrite.config

trait MapRewriteConfig[A] extends RewriteConfig[A] {

  def ruleMap: Map[String, RewriteRule[A]]

  def apply(key: String): RewriteRule[A] = ruleMap.getOrElse(key, defaultRule)

}