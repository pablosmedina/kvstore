package ckite.kvstore

import ckite.rpc.ReadCommand

case class Get(key: String) extends ReadCommand[Option[String]]