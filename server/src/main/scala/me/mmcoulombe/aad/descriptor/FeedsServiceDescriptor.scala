package me.mmcoulombe.aad.descriptor

import me.mmcoulombe.add.models.RSSFeed

import scala.concurrent.Future

/**
  * Created by mmcoulombe on 30/06/17.
  */
trait FeedsServiceDescriptor {
  def getFeeds(): Future[Iterable[RSSFeed]]
  def getFeedById(id: Long): Future[Either[String, RSSFeed]]

  def createFeed(feed: RSSFeed): Future[Either[String, RSSFeed]]
  def updateFeed(id: Long, feed: RSSFeed): Future[Either[String, RSSFeed]]
  def deleteFeed(id: Long): Future[Either[String, Int]]
}
