package com.gabriellimagomes.blockchaintransfer
package creator

import java.security.{KeyPair, PrivateKey}

trait AccountCreator {

  def newAccount(): (Account, PrivateKey) = {
    val keyPair: KeyPair = Crypto.generateKeyPair()
    (Account(keyPair.getPublic), keyPair.getPrivate)
  }
}
