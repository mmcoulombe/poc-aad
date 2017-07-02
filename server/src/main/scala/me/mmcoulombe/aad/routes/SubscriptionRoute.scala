package me.mmcoulombe.aad.routes

import akka.http.scaladsl.server.{Directives, Route}
import me.mmcoulombe.aad.services.SubscriptionService

import scala.concurrent.ExecutionContext

/**
  * Created by mmcoulombe on 30/06/17.
  */
class SubscriptionRoute(service: SubscriptionService)(implicit ec: ExecutionContext) extends Directives {
  val route: Route = {
    path("subscription") {
      handleWebSocketMessages(service.manageEvent())
    }
  }
}
