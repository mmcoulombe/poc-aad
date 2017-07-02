package me.mmcoulombe.aad.descriptor

import akka.http.scaladsl.model.ws.Message
import akka.stream.scaladsl.Flow

trait SubscriptionServiceDescriptor {
  def manageEvent(): Flow[Message, Message, Any]
  def reportErrorsFlow[T]: Flow[T, T, Any]
}
