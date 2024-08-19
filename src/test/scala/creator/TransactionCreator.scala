package com.gabriellimagomes.blockchaintransfer
package creator

import org.bouncycastle.util.encoders.Base64

import java.security.PrivateKey

trait TransactionCreator {

  def newValidTransactionRequest(
                                  sourceAccount: Account,
                                  destinationAccount: Account,
                                  privateKey: PrivateKey,
                                  amount: BigDecimal
                                ): TransactionRequest = {
    val source = Base64.toBase64String(sourceAccount.address.getEncoded)
    val destination = Base64.toBase64String(destinationAccount.address.getEncoded)
    val signature = Crypto.sign(s"$source$destination$amount", privateKey)
    TransactionRequest(source, destination, amount, signature)
  }

  def newWronglySignedTransactionRequest(sourceAccount: Account,
                                         destinationAccount: Account,
                                         amount: BigDecimal
                                        ): TransactionRequest = {
    val source = Base64.toBase64String(sourceAccount.address.getEncoded)
    val destination = Base64.toBase64String(destinationAccount.address.getEncoded)
    TransactionRequest(source, destination, amount, Crypto.sign("wrongsignature", Crypto.generateKeyPair().getPrivate))
  }
}
