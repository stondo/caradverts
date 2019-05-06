package v1.caradvert

import javax.inject.Inject
import models.slick.CarAdvert
import models.validation.CarAdvertFormInput
import play.api.Logger
import play.api.data.{Form, FormError}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, Result}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Takes HTTP requests and produces JSON.
  */
class CarAdvertController @Inject()(cc: CarAdvertControllerComponents)(implicit ec: ExecutionContext)
    extends CarAdvertBaseController(cc) {

  implicit val carAdvertFormat: OFormat[CarAdvert] = Json.format[CarAdvert]

  val logger = Logger(getClass)

  private[this] val noResourceFound: String = cc.config.get[String]("noResourceFound")

  private[this] val noResourceFoundJsonError = Json.toJson(Map("error" -> noResourceFound))

  private[this] def buildBadFormErrorMsg(e: FormError): FormError = if (e.key == "") e.copy("error") else e

  private[this] def buildUpdateOrDeleteResult(result: Int): Result =
    if (result == 1) NoContent
    else NotFound(noResourceFoundJsonError)

  def index(sortByField: Option[String] = None): Action[AnyContent] = CarAdvertAction.async { implicit request =>
    logger.trace(s"index: $sortByField")

    carAdvertResourceHandler.find(sortByField).map { carAdverts =>
      Ok(Json.toJson(carAdverts))
    }
  }

  def process: Action[AnyContent] = CarAdvertAction.async { implicit request =>
    logger.trace("process: ")
    processJsonPost()
  }

  def show(id: Long): Action[AnyContent] = CarAdvertAction.async { implicit request =>
    logger.trace(s"show: id = $id")
    carAdvertResourceHandler.lookup(id).map { carAdvertOpt =>
      carAdvertOpt.fold(NotFound(noResourceFoundJsonError))(carAdvert => Ok(Json.toJson(carAdvert)))

    }
  }

  def update(id: Long): Action[AnyContent] = CarAdvertAction.async { implicit request =>
    logger.trace(s"update: id = $id")
    processJsonPut(id)
  }

  def delete(id: Long): Action[AnyContent] = CarAdvertAction.async { implicit request =>
    logger.trace(s"delete: id = $id")
    carAdvertResourceHandler.delete(id).map(buildUpdateOrDeleteResult)
  }

  private def processJsonPost[A]()(implicit request: CarAdvertRequest[A]): Future[Result] = {
    def failure(badForm: Form[CarAdvertFormInput]): Future[Result] = {
      Future.successful(BadRequest(badForm.copy(errors = badForm.errors.map(buildBadFormErrorMsg)).errorsAsJson))
    }

    def success(input: CarAdvertFormInput): Future[Result] = {
      carAdvertResourceHandler.create(cc.carAdvertValidation.toCarAdvertRefined(input)).map(_ => Created)
    }

    cc.carAdvertValidation.form.bindFromRequest().fold(failure, success)
  }

  private def processJsonPut[A](id: Long)(implicit request: CarAdvertRequest[A]): Future[Result] = {
    def failure(badForm: Form[CarAdvertFormInput]): Future[Result] = {
      Future.successful(BadRequest(badForm.copy(errors = badForm.errors.map(buildBadFormErrorMsg)).errorsAsJson))
    }

    def success(input: CarAdvertFormInput): Future[Result] = {
      carAdvertResourceHandler
        .update(id, cc.carAdvertValidation.toCarAdvertRefined(input))
        .map(buildUpdateOrDeleteResult)
    }

    cc.carAdvertValidation.form.bindFromRequest().fold(failure, success)
  }

}
