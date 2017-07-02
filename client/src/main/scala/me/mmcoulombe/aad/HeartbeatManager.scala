package me.mmcoulombe.aad

import org.scalajs.dom.raw.WebSocket
import scala.scalajs.js.timers._

/**
  * Created by mmcoulombe on 02/07/17.
  */
class HeartbeatManager(socket: WebSocket) {
  val un = setInterval(4000)({
    if (socket.readyState == WebSocket.OPEN)
      socket.send(":") // heartbeat
  })

  def stop(): Unit =
    clearInterval(un)
}
