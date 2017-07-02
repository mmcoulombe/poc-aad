package me.mmcoulombe.aad.routes

import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import me.mmcoulombe.aad.descriptor.FeedsServiceDescriptor
import me.mmcoulombe.add.json.JsonSupport
import me.mmcoulombe.add.models.RSSFeed

import scala.concurrent.ExecutionContext

class FeedsRoute(service: FeedsServiceDescriptor)(implicit ec: ExecutionContext) extends Directives with JsonSupport {
  final val route: Route = {
    pathPrefix("rss") {
      pathEndOrSingleSlash {  // api/v1/rss
        get {
          complete(service.getFeeds())
        } ~
        post {
          entity(as[RSSFeed]) { feed =>
            complete(feed)//service.createFeed(feed))
          }
        }
      } ~
      pathPrefix(IntNumber) { id => // api/v1/rss/:id
        get {
          complete(service.getFeedById(id))
        } ~
        patch {
          entity(as[RSSFeed]) { feed =>
            complete(service.updateFeed(id, feed))
          }
        } ~
        delete {
          complete(
            service.deleteFeed(id).map(_.fold(_.toString, _.toString))
          )
        }
      }
    }
  }
}
