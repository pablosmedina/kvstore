package ckite.kvstore.http

import ckite.Cluster
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.RichHttp
import com.twitter.finagle.http.Request
import com.twitter.finagle.http.Http
import java.net.InetSocketAddress
import com.twitter.util.Closable
import ckite.Raft
import com.typesafe.config.ConfigFactory

class HttpServer(raft: Raft) {
  
  var closed = false
  var server: Closable = _
  
  def start() = {
    val restServerPort = ConfigFactory.load().getString("ckite.listen-address").split(":")(1).toInt + 1000
    val adminServerPort = restServerPort + 1000
     server = ServerBuilder()
      .codec(RichHttp[Request](Http()))
      .bindTo(new InetSocketAddress(restServerPort))
      .name("HttpServer")
      .build(new HttpService(raft))
  }
  
  def stop() = synchronized {
    if (!closed) {
    	server.close()
    	closed = true
    }
  }
  
}

object HttpServer {
  def apply(raft: Raft) = new HttpServer(raft)
}
