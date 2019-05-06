package v1.caradvert

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class CarAdvertRouter @Inject()(controller: CarAdvertController) extends SimpleRouter {

  private[this] def parseLong(s: String): Option[Long] = try { Some(s.toLong) } catch { case _: Throwable => None }

  private[this] def parsedId(id: String): Long = parseLong(id).getOrElse(0)

  override def routes: Routes = {

    case GET(q"sortBy=$sortBy") =>
      controller.logger.trace(s"sortField: $sortBy")
      controller.index(Some(sortBy))

    case GET(p"/") =>
      controller.index()

    case POST(p"/") =>
      controller.process

    case PUT(p"/$id") =>
      controller.update(parsedId(id))

    case DELETE(p"/$id") =>
      controller.delete(parsedId(id))

    case GET(p"/$id") =>
      controller.show(parsedId(id))
  }

}
