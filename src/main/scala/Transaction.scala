package com.gabriellimagomes.blockchaintransfer

import java.math.BigInteger
import java.security.SignatureException
import scala.util.{Failure, Success, Try}

case class Transaction(source: Account, destination: Account, amount: BigDecimal, nonce: BigInteger, signature: String)

case class TransactionRequest(source: String, destination: String, amount: BigDecimal, signature: String) {

  def transformToTransaction(): Try[Transaction] = {
    val sourcePublicKey = Crypto.decodePublicKey(source)

    if (!Crypto.verifySignature(sourcePublicKey, signature, s"$source$destination$amount")) {
      return Failure(SignatureException("Invalid signature"))
    }

    Success(Transaction(
      source = Account(sourcePublicKey),
      destination = Account(Crypto.decodePublicKey(destination)),
      amount = amount,
      nonce = Crypto.generateNonce(),
      signature = signature
    ))
  }
}