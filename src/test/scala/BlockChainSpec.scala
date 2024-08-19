package com.gabriellimagomes.blockchaintransfer

import creator.{AccountCreator, TransactionCreator}

import org.scalatest.BeforeAndAfterEach
import org.scalatest.flatspec.AnyFlatSpec

import java.time.Duration
import scala.math.BigDecimal

class BlockChainSpec extends AnyFlatSpec with TransactionCreator with AccountCreator with BeforeAndAfterEach {

  val (sourceAccount, sourcePrivateKey) = newAccount()
  val (destinationAccount, _) = newAccount()

  override def beforeEach(): Unit = {
    BalanceLedger.resetLedgerWithBalance(Map(
      sourceAccount -> BigDecimal(10.0),
      destinationAccount -> BigDecimal(10.0)
    ))
  }

  "A valid transaction" should "add the transaction in the blockchain and the ledger be updated" in {
    val blockChain = BlockChain(Duration.ofMillis(200))
    val t = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(10.0))
    val result = blockChain.applyTransaction(t)

    assert(result.isSuccess)
    assert(BalanceLedger.balance(destinationAccount) === BigDecimal(20.0))
    assert(BalanceLedger.balance(sourceAccount) === BigDecimal(0))
  }

  "A not signed transaction" should "return an signature exception" in {
    val blockChain = BlockChain(Duration.ofMillis(200))
    val t = newWronglySignedTransactionRequest(sourceAccount, destinationAccount, BigDecimal(10.0))
    val result = blockChain.applyTransaction(t)

    assert(result.isFailure)
    assert(result.failed.get.getMessage === "Invalid signature")
    assert(BalanceLedger.balance(destinationAccount) === BigDecimal(10.0))
    assert(BalanceLedger.balance(sourceAccount) === BigDecimal(10.0))
  }

  "A transaction from a source without funds" should "return an insufficient funds exception" in {
    val blockChain = BlockChain(Duration.ofMillis(200))
    val t = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(50.0))
    val result = blockChain.applyTransaction(t)

    assert(result.isFailure)
    assert(result.failed.get.getMessage === "Insufficient funds")
    assert(BalanceLedger.balance(destinationAccount) === BigDecimal(10.0))
    assert(BalanceLedger.balance(sourceAccount) === BigDecimal(10.0))
  }

  "Successive transactions" should "be placed in different blocks and the ledger be updated" in {
    val blockChain = BlockChain(Duration.ofMillis(1))
    val t1 = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(1.0))
    var result = blockChain.applyTransaction(t1)
    assert(result.isSuccess)

    val t2 = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(1.0))
    result = blockChain.applyTransaction(t2)
    assert(result.isSuccess)

    val t3 = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(1.0))
    result = blockChain.applyTransaction(t3)
    assert(result.isSuccess)

    val t4 = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(1.0))
    result = blockChain.applyTransaction(t4)
    assert(result.isSuccess)

    val t5 = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(1.0))
    result = blockChain.applyTransaction(t5)
    assert(result.isSuccess)

    val t6 = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(1.0))
    result = blockChain.applyTransaction(t6)
    assert(result.isSuccess)

    val t7 = newValidTransactionRequest(sourceAccount, destinationAccount, sourcePrivateKey, BigDecimal(1.0))
    result = blockChain.applyTransaction(t7)
    assert(result.isSuccess)
  }
}
