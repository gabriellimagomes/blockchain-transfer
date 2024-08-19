package com.gabriellimagomes.blockchaintransfer
package rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import spray.json._

import java.security.SignatureException
import java.time.Duration
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val transactionFormat: RootJsonFormat[TransactionRequest] = jsonFormat4(TransactionRequest.apply)
}

object Api extends JsonSupport {

  implicit val system: ActorSystem = ActorSystem("api")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer = Materializer(system)

  private val blockChain = BlockChain(Duration.ofMinutes(5))

  val transactionRoute: Route =
    path("transaction") {
      post {
        entity(as[TransactionRequest]) { request =>
          blockChain.applyTransaction(request) match {
            case Success(currentBalance) => complete(StatusCodes.OK, s"Transaction completed. Your balance is $$$currentBalance")
            case Failure(ex: SignatureException) => complete(StatusCodes.Forbidden, ex.getMessage)
            case Failure(ex) => complete(StatusCodes.BadRequest, s"Failed due ${ex.getMessage}")
          }
        }
      }
    }

  def main(args: Array[String]): Unit = {
    val port = 8080
    val bindingFuture = Http().newServerAt("localhost", port).bind(transactionRoute)

    println(s"Server online at http://localhost:$port/")
    bindingFuture.onComplete {
      case Success(_) => println("Server started successfully")
      case Failure(exception) => println(s"Failed to bind endpoint, terminating. Exception: $exception")
    }
  }
}
