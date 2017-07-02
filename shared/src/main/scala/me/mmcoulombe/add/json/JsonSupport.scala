package me.mmcoulombe.add.json

import me.mmcoulombe.add._
import me.mmcoulombe.add.models.RSSFeed
import play.api.libs.json._

trait JsonSupport {
  implicit val feedsFormat: Format[RSSFeed] = Json.format[RSSFeed]
  implicit val opFormat: Format[Operation] = Json.format[Operation]
}
