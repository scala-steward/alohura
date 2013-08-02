package alohura.matcher

import scala.xml.{ Elem, Source, XML }

import org.specs2.matcher.{
  Expectable,
  Matcher,
  MatchResult,
  MatchersImplicits
}

import javax.xml.parsers.SAXParserFactory

trait ContentMatcher { matchers: MatchersImplicits ⇒
  lazy val parser = {
    val inst = SAXParserFactory.newInstance

    inst.setValidating(true)
    inst.newSAXParser
  }

  def beValidXML[A: ToInputStream] = beValidXMLWith(_ ⇒ 1 === 1) //hum..

  def beValidXMLWith[A](f: Elem ⇒ MatchResult[_])(implicit T: ToInputStream[A]) = new Matcher[A] {
    def apply[S <: A](e: Expectable[S]) = try {
      val xml = XML.loadXML(Source.fromInputStream(T(e.value)), parser)
      val r = f(xml).toResult

      result(r.isSuccess,
        s"xml is valid and ${r.message}",
        s"xml is valid but ${r.message}",
        e)
    } catch {
      case err: Throwable ⇒
        result(false,
          "",
          s"Invalid XML: ${err.getMessage}",
          e)
    }
  }
}
