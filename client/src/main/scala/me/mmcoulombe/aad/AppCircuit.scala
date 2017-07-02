package me.mmcoulombe.aad

import diode.ActionResult.NoChange
import diode.{Action, ActionHandler, ActionResult, Circuit}
import diode.data.PotState._
import diode.data.{Pot, PotAction, Ready}
import diode.react.ReactConnector
import me.mmcoulombe.add.models.RSSFeed

/**
  * Application model
  * @param feeds the feeds to display
  */
case class Model(feeds: Pot[Map[Long, RSSFeed]])

// ==== Action ====
case class FetchFeeds(potResult: Pot[Map[Long, RSSFeed]] = Pot.empty) extends PotAction[Map[Long, RSSFeed], FetchFeeds] {
  def next(newResult: Pot[Map[Long, RSSFeed]]) = FetchFeeds(newResult)
}
case class SaveFeeds(feeds: Iterable[RSSFeed]) extends Action
case class AddFeed(feed: RSSFeed) extends Action
case class UpdateFeed(id: Long, feed: RSSFeed) extends Action
case class RemoveFeed(id: Long) extends Action

/**
  * The state manager instance
  */
object AppCircuit extends Circuit[Model] with ReactConnector[Model] {
  override def initialModel: Model = Model(Pot.empty)

  /**
    * Handler for a set of action related to the feeds
    */
  val feedsHandler = new ActionHandler(zoomTo(_.feeds)) {
    override def handle: PartialFunction[Any, ActionResult[Model]] = {
      case SaveFeeds(feeds) =>
        val feedsMap = feeds.map(f => f.id -> f).toMap
        updated(Ready(feedsMap))

      case AddFeed(feed) =>
        val map = feed.id -> feed
        updated(value.map(_ + map))

      case UpdateFeed(id, feed) =>
        updated(value.map(_.updated(id, feed)))

      case RemoveFeed(id) =>
        updated(value.map(_ - id))

      case a: FetchFeeds =>
        a.handle {
          case PotEmpty =>
            updated(value.pending(), AppEffects.fetchFeeds())
          case PotPending =>
            NoChange
          case PotReady =>
            updated(a.potResult)
          case PotUnavailable =>
            updated(value.unavailable())
          case PotFailed =>
            updated(value.fail(new Error("Error while getting feeds")))
        }
    }
  }

  /** the reducer for all handlers */
  override val actionHandler: HandlerFunction = composeHandlers(feedsHandler)
}

