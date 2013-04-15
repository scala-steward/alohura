package alohura
package matcher

import io.{ NetworkService, ToContent, Method }

import org.specs2.matcher._

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

trait NetworkMatcher extends NetworkService {

  def beResolvedAs(f: String ⇒ MatchResult[_]) = beResolvedWithin(5000)(f)

  def beResolvedWithin(timeout: Int)(f: String ⇒ MatchResult[_]) = new Matcher[String] {
    def apply[S <: String](e: Expectable[S]) = doPing(e.value, timeout) match {
      case Right(addr) ⇒
        val r = f(addr).toResult

        result(r.isSuccess,
          s"${e.description} is resolved at $addr and ${r.message}",
          s"${e.description} is resolved at $addr but ${r.message}",
          e)
      case Left(msg) ⇒
        result(false,
          "",
          s"${e.description} can't be resolved: $msg",
          e)
    }
  }

  def beRespondedWith[A](method: Method, duration: Duration = Duration(5, SECONDS))(f: A ⇒ MatchResult[_])(implicit T: ToContent[A]) = new Matcher[String] {
    def apply[S <: String](e: Expectable[S]) = try {
      val a = Await.result(doRequest(e.value, method), duration) // we could supply a max duration here
      val r = f(a).toResult

      result(r.isSuccess,
        s"url respond and ${r.message}",
        s"url respond but ${r.message}",
        e)
    } catch {
      case ex: Throwable ⇒
        result(false,
          "",
          s"url doesn't respond: ${e.value} (${ex.getMessage})",
          e)
    }
  }
}
