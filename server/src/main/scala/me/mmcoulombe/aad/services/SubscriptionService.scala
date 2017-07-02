package me.mmcoulombe.aad.services

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import me.mmcoulombe.aad.EventManager
import me.mmcoulombe.aad.descriptor.SubscriptionServiceDescriptor
import me.mmcoulombe.add.json.JsonSupport
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Failure

class SubscriptionService(eventManager: EventManager)
                         (implicit ec: ExecutionContext, materialize: ActorMaterializer) extends SubscriptionServiceDescriptor with JsonSupport {
  def manageEvent(): Flow[Message, Message, Any] =
    Flow[Message]
      .mapAsync(1) {
        case TextMessage.Strict(msg) => Future.successful(msg)
        case streamed: TextMessage.Streamed => streamed.textStream.runFold("")(_ + _)
      }.via(eventManager.flow)
      .map(msg => TextMessage.Strict(Json.stringify(Json.toJson(msg))))

  def reportErrorsFlow[T]: Flow[T, T, Any] =
    Flow[T]
      .watchTermination()((_, f) => f.onComplete {
        case Failure(err) =>
          println(s"WS stream failed : $err")
        case _ =>
      })
}
