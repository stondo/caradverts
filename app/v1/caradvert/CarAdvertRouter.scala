package v1.caradvert

import java.util.UUID

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class CarAdvertRouter @Inject()(controller: CarAdvertController) extends SimpleRouter {
  val prefix = "/v1/caradvert"

  override def routes: Routes = {
//    case GET(p"/") =>
//      controller.index

    case POST(p"/") =>
      controller.process

//    case PUT(p"/") =>
//      controller.update
//
//    case DELETE(p"/$id") =>
//      controller.delete

//    case GET(p"/$id") =>
//      controller.show(id)
  }

}
