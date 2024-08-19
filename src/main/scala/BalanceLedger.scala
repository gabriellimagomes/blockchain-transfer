package com.gabriellimagomes.blockchaintransfer

import exception.InsufficientFunds

import scala.collection.mutable
import scala.math.BigDecimal
import scala.util.{Failure, Success, Try}

object BalanceLedger {

  private val balanceByAccount = mutable.Map.empty[Account, BigDecimal].withDefaultValue(BigDecimal(0))

  def applyTransaction(transaction: Transaction): Try[Transaction] = {
    if (balanceByAccount(transaction.source) >= transaction.amount) {
      balanceByAccount(transaction.source) -= transaction.amount
      balanceByAccount(transaction.destination) += transaction.amount
      Success(transaction)
    } else {
      Failure(InsufficientFunds())
    }
  }

  def balance(account: Account): BigDecimal = balanceByAccount(account)

  def resetLedgerWithBalance(newBalance: Map[Account, BigDecimal]): Unit = {
    balanceByAccount.clear()
    balanceByAccount ++= newBalance
  }
}
