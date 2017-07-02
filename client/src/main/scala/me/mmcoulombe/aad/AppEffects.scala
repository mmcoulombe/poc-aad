package me.mmcoulombe.aad

import diode.{Effect, NoAction}
import me.mmcoulombe.add.json.JsonSupport
import me.mmcoulombe.add.models.RSSFeed
import org.scalajs.dom.ext.{Ajax, AjaxException}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Package object for all side effects of the application
  * A side effect is an asynchronous action that we don't know where
  * we'll get the answer. For instance a HTTP request.
  */
object AppEffects extends JsonSupport {
  final val HOST: String = "http://localhost:8080"
  final val REST_ROUTE: String = "api/v1"

  final val FEED_URL: String = s"$HOST/$REST_ROUTE/rss"

  /**
    * Ask for the current list of feeds to display.
    * Once obtained, it'll call the appropriate action to fill the model
    */
  def fetchFeeds(): Effect = {
    Effect(
      Ajax.get(FEED_URL)
        .map(xhr => Right(xhr.responseText))
        .recover{ case e: AjaxException => Left(new Error(e.xhr.responseText))}
        .map(
            _.map(str => Json.parse(str).as[List[RSSFeed]])
            .fold(_ => NoAction, feeds => SaveFeeds(feeds))
        )
    )
  }
}
