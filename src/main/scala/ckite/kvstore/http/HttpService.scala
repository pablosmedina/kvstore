package ckite.kvstore.http

import com.twitter.finagle.Service
import com.twitter.finagle.http.Request
import com.twitter.finagle.http.Response
import com.twitter.finagle.http.path.Path
import com.twitter.util.Future
import com.twitter.finagle.http.path.Root
import com.twitter.finagle.http.Method
import com.twitter.util.Future
import com.twitter.finagle.http.{ Http, RichHttp, Request, Response }
import com.twitter.finagle.http.Status._
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.path._
import com.twitter.finagle.{ Service, SimpleFilter }
import com.twitter.finagle.builder.{ Server, ServerBuilder }
import ckite.Cluster
import ckite.RLog
import com.twitter.util.FuturePool
import java.util.concurrent.Executors
import com.twitter.finagle.http.HttpMuxer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import ckite.CKite
import ckite.kvstore.Get
import ckite.kvstore.Put

class HttpService(ckite: CKite) extends Service[Request, Response] {

  val futurePool = FuturePool(Executors.newFixedThreadPool(8))
  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)
  val printer = new DefaultPrettyPrinter
  printer.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter)
  val writer = mapper.writer(printer)

  def apply(request: Request) = {
    request.method -> Path(request.path) match {
      case Method.Get -> Root / "status" => futurePool {
        val response = Response()
        response.contentString = writer.writeValueAsString(ckite.status)
        response
      }
      case Method.Get -> Root / "kv" / key  => futurePool {
        val localOption = request.params.getBoolean("local")
        val response = Response()
        val get = Get(key)
        val result = if (localOption.getOrElse(false)) ckite.readLocal[String](get) else ckite.read[String](get)
        response.contentString = s"$result\n"
        response
      }
      case Method.Post -> Root / "kv" / key / value => futurePool {
        val response = Response()
        val result = ckite.write[String](Put(key, value))
        response
      }
      case Method.Post -> Root / "members" / binding => futurePool {
        val response = Response()
        val result = ckite.addMember(binding)
        response
      }
      case Method.Delete -> Root / "members" / binding => futurePool {
        val response = Response()
        val result = ckite.removeMember(binding)
        response
      }
      case _ =>
        Future value Response(Http11, NotFound)
    }
  }
}