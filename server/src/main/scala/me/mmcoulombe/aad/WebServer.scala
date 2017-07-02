package me.mmcoulombe.aad

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import me.mmcoulombe.aad.services.{FeedsServices, SubscriptionService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object WebServer {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("server")
    implicit val materializer = ActorMaterializer()

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")

    // Event manager
    val eventManager = new EventManager

    // models services
    val feedServices = new FeedsServices(eventManager)
    val subscriptionService = new SubscriptionService(eventManager)

    // routes
    val service = new WebService(feedServices, subscriptionService)

    Http()
      .bindAndHandle(service.marshalledRoute, interface, port)
      .onComplete {
        case Success(binding) =>
          val address = binding.localAddress
          println(s"Server is listening on ${address.getHostName}:${address.getPort}")
        case Failure(e) =>
          println(s"Error launching akka-http: ${e.getMessage}")
          system.terminate()
      }
  }
}
