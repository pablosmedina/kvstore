package ckite.kvstore

import ckite.rpc.ReadCommand

case class Get[Key](key: Key) extends ReadCommand