package me.mmcoulombe.aad

import scala.annotation.elidable
import scala.annotation.elidable._
import scala.scalajs.js.Date

/**
  * Simple console logger
  */
object SimpleLogger {
  @elidable(FINEST)
  def trace(msg: String): Unit =
    println(s"TRACE [${new Date()}: $msg")

  @elidable(FINE)
  def debug(msg: String): Unit =
    println(s"DEBUG [${new Date()}: $msg")

  @elidable(INFO)
  def info(msg: String): Unit =
    println(s"INFO [${new Date()}: $msg")
}
