package ckite.kvstore

import ckite.CKiteBuilder
import ckite.kvstore.http.HttpServer

object KVStoreBootstrap extends App {
  val ckite = CKiteBuilder().stateMachine(new KVStore()).build
  ckite start
  val http = HttpServer(ckite)
  http.start
}
