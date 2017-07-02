package me.mmcoulombe.aad.components

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.{BackendScope, Unmounted}
import japgolly.scalajs.react.vdom.html_<^._
import me.mmcoulombe.add.models.RSSFeed

/**
  * Component used to display the feeds
  */
object FeedsView {
  case class Props(feeds: Iterable[RSSFeed])
  class Backend($: BackendScope[Props, Unit]) {
    def render(props: Props): VdomElement = {
      <.ul(
        props.feeds
          .toTagMod(feed => <.li(feed.msg))
      )
    }
  }
  private val component = ScalaComponent.builder[Props]("FeedsView")
    .stateless
    .renderBackend[Backend]
    .build

  def apply(feeds: Iterable[RSSFeed]): Unmounted[Props, Unit, Backend] =
    component(Props(feeds))
}
