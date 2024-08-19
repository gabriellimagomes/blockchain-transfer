package com.gabriellimagomes.blockchaintransfer

import org.bouncycastle.asn1.x9.{ECNamedCurveTable, X9ECParameters}
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64

import java.math.BigInteger
import java.security.*
import java.security.spec.{ECGenParameterSpec, X509EncodedKeySpec}

object Crypto {
  Security.addProvider(new BouncyCastleProvider)

  def generateKeyPair(): KeyPair = {
    val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC")
    val ecSpec = new ECGenParameterSpec("secp256r1")
    keyPairGenerator.initialize(ecSpec, new SecureRandom())
    keyPairGenerator.generateKeyPair()
  }

  def sign(data: String, privateKey: PrivateKey): String = {
    val signature = Signature.getInstance("SHA256withECDSA", "BC")
    signature.initSign(privateKey)
    signature.update(data.getBytes())
    Base64.toBase64String(signature.sign())
  }

  def generateNonce(): BigInteger = {
    val ecParams: X9ECParameters = ECNamedCurveTable.getByName("secp256r1")
    val curve = new ECDomainParameters(ecParams.getCurve, ecParams.getG, ecParams.getN, ecParams.getH)
    generateNonce(curve.getN)
  }

  private def generateNonce(order: BigInteger): BigInteger = {
    val random = new SecureRandom()
    var nonce = new BigInteger(order.bitLength(), random)
    while (nonce.compareTo(BigInteger.ZERO) <= 0 || nonce.compareTo(order) >= 0) {
      nonce = new BigInteger(order.bitLength(), random)
    }
    nonce
  }

  def verifySignature(publicKey: PublicKey, signature: String, data: String): Boolean = {
    val sig = Signature.getInstance("SHA256withECDSA", "BC")
    sig.initVerify(publicKey)
    sig.update(data.getBytes())
    sig.verify(Base64.decode(signature))
  }

  def decodePublicKey(encodedKey: String): PublicKey = {
    val keyBytes = Base64.decode(encodedKey)
    val keySpec = new X509EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance("ECDSA", "BC")
    keyFactory.generatePublic(keySpec)
  }
}
