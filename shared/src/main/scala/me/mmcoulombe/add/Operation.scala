package me.mmcoulombe.add

import me.mmcoulombe.add.models.RSSFeed

case class Operation(op: String, path: String, from: Option[String] = None, value: Option[RSSFeed] = None)
