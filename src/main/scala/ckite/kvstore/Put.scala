package ckite.kvstore

import ckite.rpc.WriteCommand

case class Put(key: String, value: String) extends WriteCommand[String]