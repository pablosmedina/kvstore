package ckite.kvstore

import ckite.CKiteBuilder
import ckite.kvstore.http.HttpServer
import ckite.mapdb.MapDBStorage
import ckite.rpc.FinagleThriftRpc

object KVStoreBootstrap extends App {
  val ckite = CKiteBuilder().stateMachine(new KVStore()).rpc(FinagleThriftRpc).storage(MapDBStorage()).build
  ckite.start()
  val http = HttpServer(ckite)
  http.start()
}