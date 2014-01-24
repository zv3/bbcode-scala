[![Build Status](https://travis-ci.org/daydev/bbcode-scala.png?branch=master)](https://travis-ci.org/daydev/bbcode-scala)

## Description

Library to parse and rewrite (compile) BBCode.
There is currently implementation only for rewrite into html.
This library does not use regex replace, it builds AST (abstract syntax tree) first and then compiles (rewrites) it.
So it should be relatively simple to replace rewrite part with some other format. 

## Dependency

At the time library is not hosted in any repositories, so to use it you should either build it and
manage jar-file manually or use sbt's ability to work with git repositories directly.
For the latter option, modify your `Build.scala` like so:


```scala
...

lazy val bbcode = RootProject(uri("git://github.com/daydev/bbcode-scala.git#master"))

lazy val main = Project(/*your main project declaration*/).dependsOn(bbcode)

...
```
sbt will clone repository, build project, and add jar-file to your dependency jars

## How to use

Simple use
```scala

import org.daydev.scala.bb.rewrite.DefaltBbRewriter.rewrite

val bbcode: String = ... //your bbcode
val html = rewrite(bbcode)

```
You get html (`Seq[NodeSeq]`) rewritten with default configuration.
Default configuration provides support for:

* \[b\] \(&lt;strong&gt;\)
* \[br\]
* \[code\] \(&lt;pre&gt; with text inside as is without bbcode rewrite\)
* \[color\] \(&lt;span&gt; with inline style\)
* \[font\] \(&lt;span&gt; with inline style\)
* \[i\] \(&lt;em&gt;\)
* \[img\] \(bb tag attr will become alt text, bb tag content will become src. E.g. \[img=a\]b\[/img\] -&gt; &lt;img alt="a" src="b" /&gt;\)
* \[li\]
* \[list\] \(list marker type can be provided as bb tag attr, e.g. \[list=1\] -&gt; &lt;ol type="1"&gt;; if no type -&gt; &lt;ul&gt;\)
* \[quote\] \(&lt;blockquote&gt;\)
* \[s\] \(&lt;del&gt;\)
* \[size\] \(span with inline style, if units provided \(e.g \[size=1.2em\]\) will use them, else pixels assumed \(\[size=10\] == \[size=10px\]\)\)
* \[sub\]
* \[sup\]
* \[tt\] \(span with monospace font style\)
* \[u\] \(&lt;ins&gt;\)
* \[url\]

All user text is passed through scala xml literals, so it is properly escaped in resulting string.

## How to extend

Rewriter uses implicit `RewriteConfig` configuration. `RewriteConfig` is effectively just a function from `String` (bbcode tag name) to `RewriteRule`.
And `RewriteRule` is just a function `(BbTag, Seq[NodeSeq]) => NodeSeq` which takes current tag and prerewritten children of this tag and produces rewrite for this tag.
In addition `RewriteConfig` provides utility methods `addRule`, `updateRule` and `removeRule`.
You can create `RewriteConfig[NodeSeq]` from `Map[String, RewriteRule[NodeSeq]]` via `HtmlRewriteConfig(map)`

Example extension:
```scala

import org.daydev.scala.bb.rewrite.config.HtmlRewriteConfig

val myRules = Map[String, RewriteRule[NodeSeq](
        "hr" -> (tag, content) => <hr />,
        ...
    )

implicit val myConfiguration = myRules.foldLeft(HtmlRewriteConfig.default: RewriteConfig[NodeSeq]) {
    case (config, (key, rule)) => config.addRule(key)(rule)
  }
...
```
Example completely different config
```scala

import org.daydev.scala.bb.rewrite.config.HtmlRewriteConfig

val myRules = Map[String, RewriteRule[NodeSeq](
        "url" -> (tag, content) => <button class="link" data-href={ tag.attr.getOrElse("#") }>{ content }</button>,
        ...
    )

implicit val myConfiguration = HtmlRewriteConfig(myRules)
...
```


## More details about architecture

At the moment full rewrite cycle consists of three stages:

1. Tokenization. Input string is split into tokens: open bbcode tag, close bbcode tag, newline and plain text.

2. Parsing. Tokens from the previous stage are processed and build into AST of `BbElement`s.
`BbElement` can be either `BbTab` with `name`, optional `attr` and `Seq` of `BbElement`s `children`
or `PlanText` with just `content`.
On this stage unmatched tags are transformed into `PlainText`.

3. Compilation (rewrite). AST from previous stage is recursively rewritten from bottom to top according
to the set of rules provided by implicit or explicit `configuration`.
Currently only rewrite into xml/html i.e. `Seq[NodeSeq]` is implemented.

Every stage has interface and implementation trait. `DefaultBbRewriter` object then just mixes all
implementations together to produce complete rewriter `String => Seq[NodeSeq]` like so:
```scala
object DefaultBbRewriter
    extends XmlBbRewriter
    with TokenStackBbParser
    with GrammarCombinatorsBbLexer
```

Furthermore every stage depends only on stage below it and no lower, so rewriter needs parser, but knows nothing about
tokenization. This should allow for easy replacement of implementation of any stage.

`BbRewriter` interface is also generic on it's result type in case rewrite into something other than xml is needed.