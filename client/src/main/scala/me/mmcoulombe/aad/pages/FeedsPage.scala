package me.mmcoulombe.aad.pages

import diode.data.Pot
import diode.react.ModelProxy
import diode.react.ReactPot._
import japgolly.scalajs.react.{Callback, ScalaComponent}
import japgolly.scalajs.react.component.Scala.{BackendScope, Unmounted}
import japgolly.scalajs.react.vdom.html_<^._
import me.mmcoulombe.aad.FetchFeeds
import me.mmcoulombe.aad.components.FeedsView
import me.mmcoulombe.add.models.RSSFeed

/**
  * Component use render the content for the default url
  */
object FeedsPage {
  case class Props(proxy: ModelProxy[Pot[Map[Long, RSSFeed]]])
  class Backend($: BackendScope[Props, Unit]) {
    def mounted(props: Props): Callback = {
      props.proxy.dispatchCB(FetchFeeds())
    }

    def render(props: Props): VdomElement = {
      <.div(
        props.proxy().renderReady(feeds =>
          FeedsView(feeds.values)
        )
      )
    }
  }

  private val component = ScalaComponent.builder[Props]("FeedsPage")
    .stateless
    .renderBackend[Backend]
    .componentDidMount($ => $.backend.mounted($.props))
    .build

  def apply(proxy: ModelProxy[Pot[Map[Long, RSSFeed]]]): Unmounted[Props, Unit, Backend] =
    component(Props(proxy))
}
