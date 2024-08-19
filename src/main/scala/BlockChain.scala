package com.gabriellimagomes.blockchaintransfer

import java.security.KeyPair
import java.time.{Duration, LocalDateTime}
import scala.collection.mutable
import scala.collection.mutable.Queue
import scala.util.Try

class BlockChain(blockSplitPeriod: Duration) {

  private val blocks = mutable.Queue[Block]()
  private var timeMarker: Option[LocalDateTime] = None
  private val keyPair: KeyPair = Crypto.generateKeyPair()

  def applyTransaction(transactionRequest: TransactionRequest): Try[BigDecimal] = {
    transactionRequest
      .transformToTransaction()
      .flatMap { t => BalanceLedger.applyTransaction(t) }
      .map { t =>
        if (timeMarker.isEmpty || timeMarker.get.plus(blockSplitPeriod).isBefore(LocalDateTime.now())) {
          timeMarker = Some(LocalDateTime.now())
          val (nextNumber, signature) = blocks.lastOption
            .map(l => (l.number + 1L, l.sign(keyPair.getPrivate)))
            .getOrElse((1L, ""))
          blocks.enqueue(Block(number = nextNumber, priorBlockRef = signature))
        }

        blocks.last.addTransaction(t)
        BalanceLedger.balance(t.source)
      }
  }
}
