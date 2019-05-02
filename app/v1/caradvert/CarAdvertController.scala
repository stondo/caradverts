package v1.caradvert

import java.time.LocalDate
import java.util.UUID

import eu.timepit.refined.collection.NonEmpty
import javax.inject.Inject
import models.{CarAdvert, Diesel, FuelType, Gasoline, NotSelected}
import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Result}
import utils.Refinements._

import scala.concurrent.{ExecutionContext, Future}

final case class CarAdvertFormInput(title: String,
                                    fuel: FuelType,
                                    price: Int,
                                    `new`: Boolean,
                                    mileage: Option[Int] = None,
                                    `first registration`: Option[LocalDate] = None) {

  def toCarAdvert(): CarAdvert = {

    val titleRefinedEither: Either[String, Title] = Title.from(title)
    val priceRefinedEither: Either[String, Price] = Price.from(price)

    mileage.fold {
      val (titleRefined, priceRefined) =
        if (titleRefinedEither.isRight && priceRefinedEither.isRight)
          (titleRefinedEither.right.get, priceRefinedEither.right.get)
        else throw new Exception("Invalid starting value provided")

      CarAdvert(UUID.randomUUID(), titleRefined, fuel, priceRefined, `new`)

    } { mil =>
      val mileageRefinedEither = Mileage.from(mil)
      val (titleRefined, priceRefined, mileageRefined) =
        if (titleRefinedEither.isRight && priceRefinedEither.isRight && mileageRefinedEither.isRight)
          (titleRefinedEither.right.get, priceRefinedEither.right.get, mileageRefinedEither.right.get)
        else throw new Exception("Invalid starting value provided")

      CarAdvert(UUID.randomUUID(), titleRefined, fuel, priceRefined, `new`, Some(mileageRefined), `first registration`)
    }

  }

}

/**
  * Takes HTTP requests and produces JSON.
  */
class CarAdvertController @Inject()(cc: CarAdvertControllerComponents)(implicit ec: ExecutionContext)
    extends CarAdvertBaseController(cc) {

  private val logger = Logger(getClass)

  import play.api.data.format.Formatter
  import play.api.data.format.Formats._
  implicit object FuelTypeFormatter extends Formatter[FuelType] {
    override val format = Some(("format.fuelType", Nil))
    override def bind(key: String, data: Map[String, String]) =
      parsing(s => if (s.equals("GASOLINE")) Gasoline else if (s.equals("DIESEL")) Diesel else NotSelected,
              "error.fuelType",
              Nil)(key, data)
    override def unbind(key: String, value: FuelType) = Map(key -> value.toString)
  }

  private val form: Form[CarAdvertFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title"              -> nonEmptyText,
        "fuel"               -> of[FuelType],
        "price"              -> number(min = 1),
        "new"                -> boolean,
        "mileage"            -> optional(number(min = 1)),
        "first registration" -> optional(localDate)
      )(CarAdvertFormInput.apply)(CarAdvertFormInput.unapply)
    )
  }

//  def index: Action[AnyContent] = CarAdvertAction.async { implicit request =>
//    logger.trace("index: ")
//    carAdvertResourceHandler.find.map { carAdverts =>
//      Ok(Json.toJson(carAdverts))
//    }
//  }

  def process: Action[AnyContent] = CarAdvertAction.async { implicit request =>
    logger.trace("process: ")
    processJsonPost()
  }

//  def show(id: String): Action[AnyContent] = CarAdvertAction.async { implicit request =>
//    logger.trace(s"show: id = $id")
//    carAdvertResourceHandler.lookup(id).map { carAdvert =>
//      Ok(Json.toJson(carAdvert))
//    }
//  }

  private def processJsonPost[A]()(implicit request: CarAdvertRequest[A]): Future[Result] = {
    def failure(badForm: Form[CarAdvertFormInput]): Future[Result] = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: CarAdvertFormInput) = {
      carAdvertResourceHandler.create(input.toCarAdvert()).map { carAdvert =>
        Created(Json.toJson(carAdvert))
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
