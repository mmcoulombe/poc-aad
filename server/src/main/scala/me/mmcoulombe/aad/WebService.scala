package me.mmcoulombe.aad

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import me.mmcoulombe.aad.routes.{FeedsRoute, SubscriptionRoute}
import me.mmcoulombe.aad.services.{FeedsServices, SubscriptionService}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by mmcoulombe on 30/06/17.
  */
sealed class WebService(feedsServices: FeedsServices, subscriptionService: SubscriptionService)
                (implicit system: ActorSystem) extends Directives {

  private final val staticRoute = {
    pathSingleSlash {
      get {
        complete {
          me.mmcoulombe.aad.html.index.render()
        }
      }
    } ~
    pathPrefix("assets" / Remaining) { file =>
      encodeResponse {
        getFromResource("public/" + file)
      }
    }
  }

  private final val feedsRoute = new FeedsRoute(feedsServices)
  private final val subscriptionRoute = new SubscriptionRoute(subscriptionService)

  val marshalledRoute: Route = {
    staticRoute ~
    pathPrefix("api") {
      pathPrefix("v1") {
        feedsRoute.route ~
        subscriptionRoute.route
      }
    }
  }

}
