package ckite.kvstore

import java.nio.ByteBuffer

import ckite.statemachine.StateMachine
import ckite.util.Serializer

import scala.collection.mutable.Map

class KVStore extends StateMachine {

  private var map = Map[String, String]()
  private var lastIndex: Long = 0

  def applyWrite = {
    case (index, Put(key: String, value: String)) => {
      map.put(key, value)
      lastIndex = index
      value
    }
  }

  def applyRead = {
    case Get(key) => map.get(key)
  }

  def getLastAppliedIndex: Long = lastIndex

  def restoreSnapshot(byteBuffer: ByteBuffer) = {
    map = Serializer.deserialize[Map[String, String]](byteBuffer.array())
  }

  def takeSnapshot(): ByteBuffer = ByteBuffer.wrap(Serializer.serialize(map))

}