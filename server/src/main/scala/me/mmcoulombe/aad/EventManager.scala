package me.mmcoulombe.aad

import akka.actor._
import akka.stream.OverflowStrategy
import akka.stream.scaladsl._
import me.mmcoulombe.add.Operation

trait ActorEventControl
case class Connected(subscriber: ActorRef) extends ActorEventControl
case class ReceivedEvent(event: String) extends ActorEventControl
case class SendEvent(event: Operation) extends ActorEventControl
case object Disconnected extends ActorEventControl

class EventManager(implicit system: ActorSystem) {

  private val eventActor =
    system.actorOf(Props(new Actor {
      var subscribers = Set.empty[ActorRef]
      override def receive: Receive = {
        case Connected(subscriber) =>
          println("WS Event Manager: Connected")
          context.watch(subscriber)
          subscribers += subscriber

        case Disconnected => println("WS Event manager: Disconnected")

        case ReceivedEvent(event) => println(s"Received event : $event")

        case SendEvent(event) => {
          println(s"SendEvent : $event")
          dispatch(event)
        }

        case msg: Operation => dispatch(msg)
      }
      def dispatch(op: Operation): Unit = {
        println(s"dispatch ${op.op}")
        subscribers.foreach(_ ! op)
      }

    }))

  def sendMessage(event: Operation): Unit = {
    println("sendMessage")
    eventActor ! SendEvent(event)
  }

  private val in = Flow[String] // heartbeat otherwise the socket will be close after x seconds in idle
      .map(ReceivedEvent)
      .to(Sink.actorRef[ActorEventControl](eventActor, Disconnected))

  private val out =
    Source.actorRef[Operation](1, OverflowStrategy.fail)
      .mapMaterializedValue(eventActor ! Connected(_))

  val flow = Flow.fromSinkAndSource(in, out)
}
