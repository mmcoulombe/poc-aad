package me.mmcoulombe.aad

import diode.{Action, NoAction}
import japgolly.scalajs.react.vdom.html_<^._
import me.mmcoulombe.aad.pages.FeedsPage
import me.mmcoulombe.add.Operation
import me.mmcoulombe.add.json.JsonSupport

import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.raw.WebSocket
import play.api.libs.json.Json

/**
  * Entry point for our Javascript application
  */
object App extends JSApp with JsonSupport {
  def toAction(op: Operation): Action = {
    op.op match {
      case "add" => AddFeed(op.value.get)
      case "replace" => NoAction
      case "remove" => RemoveFeed(op.path.toInt)
      case _ => NoAction
    }
  }

  def main(): Unit = {
    val socket = new WebSocket("ws://localhost:8080/api/v1/subscription")

    socket.onopen = (_) => {
      println("Socket Open")
    }

    socket.onclose = (_) => {
      println("Socket closed")
    }

    socket.onerror = (e) => {
      println(s"Socket Error: $e")
    }

    socket.onmessage = (e) => {
      println(s"Message from socket ${e.data}")
      // Deserialize json string to Operation
      val jsOp = e.data.toString
      // Parse Operation
      val op = Json.parse(jsOp).as[Operation]
      // Match Operation to action (can certainly do it in a better way)
      val action = toAction(op)
      // Dispatch action matching the Operation
      AppCircuit.dispatch(action)
    }

    val connector = AppCircuit.connect(_.feeds)
    connector(p => FeedsPage(p))
      .renderIntoDOM(dom.document.getElementById("app"))
  }
}
