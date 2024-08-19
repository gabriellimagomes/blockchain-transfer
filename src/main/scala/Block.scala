package com.gabriellimagomes.blockchaintransfer

import java.security.PrivateKey
import scala.collection.mutable.ListBuffer

case class Block(number: Long, private var signature: String = "", priorBlockRef: String) {

  private val transactions: ListBuffer[Transaction] = ListBuffer.empty

  def sign(privateKey: PrivateKey): String = {
    signature = Crypto.sign(hash, privateKey)
    signature
  }

  def addTransaction(transaction: Transaction): Unit = transactions += transaction

  private def hash = s"$number${transactions.map(_.toString).mkString}$priorBlockRef"
}
