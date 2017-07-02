package me.mmcoulombe.add.json

import me.mmcoulombe.add._
import me.mmcoulombe.add.models.RSSFeed
import play.api.libs.json._

trait JsonSupport {
  implicit val feedsFormat = Json.format[RSSFeed]
  implicit val opFormat = Json.format[Operation]

  implicit class JsonString(value: String) {
    def fromJson[A]: A = {
      Json.parse(value).as[A]
    }
  }
}
