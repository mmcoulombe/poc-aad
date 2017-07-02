package me.mmcoulombe.aad

import me.mmcoulombe.add.models.RSSFeed

import scala.collection.mutable

object Model {
  val feeds = mutable.ListBuffer(
    RSSFeed(3, "The new galaxy note 8 is out!"),
    RSSFeed(2, "The Galaxy Note 8 will be 43 inches !!"),
    RSSFeed(1, "Lorem ipsum dolar sit amet")
  )

  def add(feed: RSSFeed): Either[String, RSSFeed] = {
    feeds.+=:(feed)
    Right(feed)
  }

  def getById(id: Long): Either[String, RSSFeed] =
    Model.feeds.find(_.id == id).toRight(s"Feed with id `$id` not found")

  def update(id: Long, feed: RSSFeed): Either[String, RSSFeed] = {
    Some(Model.feeds.indexWhere(_.id == id))
      .filter(_ > -1)
      .fold[Either[String, RSSFeed]](Left(s"Feed with id `$id` not found"))(index => {
        feeds(index) = feed
        Right(feed)
      })
  }

  def delete(id: Long): Either[String, Int] = {
    Model.feeds.find(_.id == id)
      .fold[Either[String, Int]](Left(s"Feed with id `$id` not found"))(feed => {
        feeds -= feed
        Right(200)
      })
  }
}
