package org.daydev.scala.bb.rewrite.config

trait RewriteConfig[A] extends Function1[String, RewriteRule[A]] {

  def addRule(key: String)(rule: RewriteRule[A]): RewriteConfig[A]

  def updateRule(key: String)(rule: RewriteRule[A]): RewriteConfig[A]

  def dropRule(key: String): RewriteConfig[A]

  def defaultRule: RewriteRule[A]
  
}