package org.daydev.scala.bb.rewrite.config

import org.scalatest.FunSuite
import org.daydev.scala.bb.rewrite.DefaultBbRewriter
import org.daydev.scala.bb.model.BbTag

class HtmlRewriteDefaultRulesSpec extends FunSuite {

  val config = implicitly[HtmlRewriteConfig]

  val rewriter = DefaultBbRewriter

  def rw(name: String, attr: Option[String] = None, content: String = "text") = rewriter.rewrite(BbTag(name, attr, content).toString)(config)

  def rw(name: String, attr: String): String = rw(name, Some(attr))

  test("should rewrite [b]") {
    assert {
      rw("b") === <strong>text</strong>.toString
    }
  }

  test("should rewrite [br]") {
    assert {
      rw("br", None, "") === <br/>.toString
    }
  }

  test("should rewrite [code]") {
    assert {
      rw("code") === <pre>text</pre>.toString
    }
  }

  test("should not rewrite bbcode inside [code]") {
    assert {
      rewriter.rewrite("[code][b][i]text[/i][/b][/code]") === <pre>[b][i]text[/i][/b]</pre>.toString
    }
  }

  test("should rewrite [color]") {
    assert {
      rw("color", "red") === <span style="color: red">text</span>.toString
    }
  }

  test("should rewrite [font]") {
    assert {
      rw("font", "arial") === <span style="font-family: arial">text</span>.toString
    }
  }

  test("should rewrite [i]") {
    assert {
      rw("i") === <em>text</em>.toString
    }
  }

  test("should rewrite [img]") {
    assert {
      rw("img", None, "http://example.com/a.jpg") ===
        <img src="http://example.com/a.jpg"/>.toString
    }
  }

  test("should rewrite [img=altText]") {
    assert {
      rw("img", Some("altText"), "http://example.com/a.jpg") ===
        <img alt="altText" src="http://example.com/a.jpg"/>.toString
    }
  }

  test("should rewirte [li]") {
    assert {
      rw("li") === <li>text</li>.toString
    }
  }

  test("should rewrite [list] as unordered list") {
    assert {
      rw("list") === <ul>text</ul>.toString
    }
  }

  test("should rewrite [list] with attr as ordered list of specified type") {
    assert {
      rw("list", "1") === <ol type="1">text</ol>.toString
    }
    assert {
      rw("list", "A") === <ol type="A">text</ol>.toString
    }
    assert {
      rw("list", "a") === <ol type="a">text</ol>.toString
    }
    assert {
      rw("list", "I") === <ol type="I">text</ol>.toString
    }
    assert {
      rw("list", "i") === <ol type="i">text</ol>.toString
    }
  }

  test("should rewrite [list] with invalid type attr as default ordered list") {
    assert {
      rw("list", "q") === <ol>text</ol>.toString
    }
  }

  test("should rewrite [quote]") {
    assert {
      rw("quote") === <blockquote>text</blockquote>.toString
    }
  }

  test("should rewrite [quote] with source") {
    assert {
      rw("quote", "source") === <blockquote cite="source">text</blockquote>.toString
    }
  }

  test("should rewrite [s]") {
    assert {
      rw("s") === <del>text</del>.toString
    }
  }

  test("should rewrite [size]") {
    assert {
      rw("size", "10px") === <span style="font-size: 10px">text</span>.toString
    }
  }

  test("should rewrite [size] without value unit specified") {
    assert {
      rw("size", "10") === <span style="font-size: 10px">text</span>.toString
    }
  }

  test("should rewrite [sub]") {
    assert {
      rw("sub") === <sub>text</sub>.toString
    }
  }

  test("should rewrite [sup]") {
    assert {
      rw("sup") === <sup>text</sup>.toString
    }
  }

  test("should rewrite [tt]") {
    assert {
      rw("tt") === <span style="font-family: monospace">text</span>.toString
    }
  }

  test("should rewrite [u]") {
    assert {
      rw("u") === <ins>text</ins>.toString
    }
  }

  test("should rewrite [url]") {
    assert {
      rw("url", None, "http://example.com") ===
        <a href="http://example.com">http://example.com</a>.toString
    }
  }

  test("should rewrite [url] with attr") {
    assert {
      rw("url", "http://example.com") ===
        <a href="http://example.com">text</a>.toString
    }
  }

}