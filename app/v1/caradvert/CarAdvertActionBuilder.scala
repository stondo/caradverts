package v1.caradvert

import javax.inject.Inject
import net.logstash.logback.marker.LogstashMarker
import play.api.{Logger, MarkerContext}
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * A wrapped request for post resources.
  *
  * This is commonly used to hold request-specific information like
  * security credentials, and useful shortcut methods.
  */
trait CarAdvertRequestHeader extends MessagesRequestHeader with PreferredMessagesProvider
class CarAdvertRequest[A](request: Request[A], val messagesApi: MessagesApi)
    extends WrappedRequest(request)
    with CarAdvertRequestHeader

/**
  * Provides an implicit marker that will show the request in all logger statements.
  */
trait RequestMarkerContext {
  import net.logstash.logback.marker.Markers

  private def marker(tuple: (String, Any)): LogstashMarker = Markers.append(tuple._1, tuple._2)

  private implicit class RichLogstashMarker(marker1: LogstashMarker) {
    def &&(marker2: LogstashMarker): LogstashMarker = marker1.and(marker2)
  }

  implicit def requestHeaderToMarkerContext(implicit request: RequestHeader): MarkerContext = {
    MarkerContext {
      marker("id" -> request.id) && marker("host" -> request.host) && marker("remoteAddress" -> request.remoteAddress)
    }
  }

}

/**
  * The action builder for the CarAdvert resource.
  *
  * This is the place to put logging, metrics, to augment
  * the request with contextual data, and manipulate the
  * result.
  */
class CarAdvertActionBuilder @Inject()(messagesApi: MessagesApi, playBodyParsers: PlayBodyParsers)(
    implicit val executionContext: ExecutionContext)
    extends ActionBuilder[CarAdvertRequest, AnyContent]
    with RequestMarkerContext
    with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type PostRequestBlock[A] = CarAdvertRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A], block: PostRequestBlock[A]): Future[Result] = {
    // Convert to marker context and use request in block
    implicit val markerContext: MarkerContext = requestHeaderToMarkerContext(request)
    logger.trace(s"invokeBlock: ")

    val future = block(new CarAdvertRequest(request, messagesApi))

    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Cache-Control" -> s"max-age: 100")
        case other =>
          result
      }
    }
  }
}

/**
  * Packages up the component dependencies for the carAdvert controller.
  *
  * This is a good way to minimize the surface area exposed to the controller, so the
  * controller only has to have one thing injected.
  */
case class CarAdvertControllerComponents @Inject()(carAdvertActionBuilder: CarAdvertActionBuilder,
                                                   carAdvertResourceHandler: CarAdvertResourceHandler,
                                                   actionBuilder: DefaultActionBuilder,
                                                   parsers: PlayBodyParsers,
                                                   messagesApi: MessagesApi,
                                                   langs: Langs,
                                                   fileMimeTypes: FileMimeTypes,
                                                   executionContext: scala.concurrent.ExecutionContext)
    extends ControllerComponents

/**
  * Exposes actions and handler to the PostController by wiring the injected state into the base class.
  */
class CarAdvertBaseController @Inject()(cacc: CarAdvertControllerComponents)
    extends BaseController
    with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = cacc

  def CarAdvertAction: CarAdvertActionBuilder = cacc.carAdvertActionBuilder

  def carAdvertResourceHandler = cacc.carAdvertResourceHandler
}
