package me.mmcoulombe.aad.services

import me.mmcoulombe.aad.{EventManager, Model}
import me.mmcoulombe.aad.descriptor.FeedsServiceDescriptor
import me.mmcoulombe.add._
import me.mmcoulombe.add.models.RSSFeed

import scala.concurrent.Future

class FeedsServices(eventManager: EventManager) extends FeedsServiceDescriptor {
  override def getFeeds(): Future[Iterable[RSSFeed]] =
    Future.successful(Model.feeds)

  override def getFeedById(id: Long): Future[Either[String, RSSFeed]] =
    Future.successful(Model.getById(id))

  override def createFeed(feed: RSSFeed): Future[Either[String, RSSFeed]] = {
    eventManager.sendMessage(Operation(op = "add", path = "feeds", value = Some(feed)))
    Future.successful(Model.add(feed))
  }

  override def updateFeed(id: Long, feed: RSSFeed): Future[Either[String, RSSFeed]] = {
    eventManager.sendMessage(Operation(op = "replace", path = s"feeds/$id", value = Some(feed)))
    Future.successful(Model.update(id, feed))
  }

  override def deleteFeed(id: Long): Future[Either[String, Int]] = {
    eventManager.sendMessage(Operation(op = "remove", s"$id"))
    Future.successful(Model.delete(id))
  }
}
