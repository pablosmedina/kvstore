package ckite.kvstore

import ckite.RaftBuilder
import ckite.kvstore.http.HttpServer

object KVStoreBootstrap extends App {
  val raft = RaftBuilder().stateMachine(new KVStore()).build
  raft start
  val http = HttpServer(raft)
  http.start
}
