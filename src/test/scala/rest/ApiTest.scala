package com.gabriellimagomes.blockchaintransfer
package rest

import creator.{AccountCreator, TransactionCreator}

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import spray.json.*

import java.security.PrivateKey

class ApiTest extends AnyWordSpec
  with Matchers
  with ScalatestRouteTest
  with JsonSupport
  with TransactionCreator
  with AccountCreator
  with BeforeAndAfterEach {

  val route: Route = Api.transactionRoute

  val (sourceAccount, sourcePrivateKey) = newAccount()
  val (destinationAccount, _) = newAccount()

  override def beforeEach(): Unit = {
    BalanceLedger.resetLedgerWithBalance(Map(
      sourceAccount -> BigDecimal(100.0),
      destinationAccount -> BigDecimal(100.0)
    ))
  }

  "Transaction post" should {

    "return OK for a valid transaction request" in {
      val transactionRequest = newValidTransactionRequest(
        sourceAccount = sourceAccount,
        destinationAccount = destinationAccount,
        privateKey = sourcePrivateKey,
        amount = BigDecimal(50.0))

      val jsonRequest = transactionRequest.toJson.toString
      Post("/transaction",  HttpEntity(ContentTypes.`application/json`, jsonRequest)) ~> route ~> check {
        status should === (StatusCodes.OK)
        responseAs[String] should include ("Transaction completed. Your balance is $50.0")
      }
    }

    "return Forbidden for a wrong source signature " in {
      val transactionRequest = newWronglySignedTransactionRequest(
        sourceAccount = sourceAccount,
        destinationAccount = destinationAccount,
        amount = BigDecimal(50.0))

      val jsonRequest = transactionRequest.toJson.toString
      Post("/transaction", HttpEntity(ContentTypes.`application/json`, jsonRequest)) ~> route ~> check {
        status should === (StatusCodes.Forbidden)
        responseAs[String] should include("Invalid signature")
      }
    }

    "return bad request for insufficient funds " in {
      val transactionRequest = newValidTransactionRequest(
        sourceAccount = sourceAccount,
        destinationAccount = destinationAccount,
        privateKey = sourcePrivateKey,
        amount = BigDecimal(200.0))

      val jsonRequest = transactionRequest.toJson.toString
      Post("/transaction", HttpEntity(ContentTypes.`application/json`, jsonRequest)) ~> route ~> check {
        status should === (StatusCodes.BadRequest)
        responseAs[String] should include("Insufficient funds")
      }
    }
  }
}
