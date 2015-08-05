package ckite.kvstore

import ckite.CKiteBuilder
import ckite.kvstore.http.HttpServer
import ckite.rpc.FinagleThriftRpc

object KVStoreBootstrap extends App {
  val ckite = CKiteBuilder().stateMachine(new KVStore()).rpc(FinagleThriftRpc).build
  ckite.start()
  val http = HttpServer(ckite)
  http.start()
}