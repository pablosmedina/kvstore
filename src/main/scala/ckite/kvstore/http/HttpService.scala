package ckite.kvstore.http

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future => ScalaFuture}
import scala.util.Failure
import scala.util.Success
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.twitter.finagle.Service
import com.twitter.finagle.http.{ Http, RichHttp, Request, Response }
import com.twitter.finagle.http.Status._
import com.twitter.finagle.http.Method
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.path._
import com.twitter.finagle.{ Service, SimpleFilter }
import com.twitter.finagle.builder.{ Server, ServerBuilder }
import com.twitter.util.Future
import com.twitter.util.Promise
import ckite.Raft
import ckite.kvstore.Get
import ckite.kvstore.Put

class HttpService(raft: Raft) extends Service[Request, Response] {

  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)
  val printer = new DefaultPrettyPrinter
  printer.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter)
  val writer = mapper.writer(printer)

  def apply(request: Request) = {
    request.method -> Path(request.path) match {
      case Method.Get -> Root / "status" => Future.value {
        val response = Response()
        response.contentString = writer.writeValueAsString(raft.status)
        response
      }
      case Method.Get -> Root / "kv" / key => {
        val localOption = request.params.getBoolean("local")
        val get = Get(key)
        val result = if (localOption.getOrElse(false)) ScalaFuture.successful(raft.readLocal(get)) else raft.read(get)
        result.map { value =>
          val response = Response()
          response.contentString = s"$value\n"
          response
        }
      }
      case Method.Post -> Root / "kv" / key / value => {
        raft.write(Put(key, value)) map { value => Response() }
      }
      case Method.Post -> Root / "members" / binding => {
        raft.addMember(binding) map { value => Response() }
      }
      case Method.Delete -> Root / "members" / binding => {
        raft.removeMember(binding) map { value => Response() }
      }
      case _ =>
        Future value Response(Http11, NotFound)
    }
  }

  private implicit def toTwitterFuture[T](scalaFuture: ScalaFuture[T]): Future[T] = {
    val promise = Promise[T]()
    scalaFuture.onComplete {
      case Success(value) => promise.setValue(value)
      case Failure(t) => promise.raise(t)
    }
    promise
  }
}