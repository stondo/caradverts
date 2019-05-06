import java.time.{LocalDate, LocalDateTime}

import models.{CarAdvertRefined, Gasoline}
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsResultException, Json, OFormat}
import eu.timepit.refined.auto._
import models.slick.CarAdvert

class CarAdvertRefinedSpec extends PlaySpec {

  implicit val carAdvertFormat: OFormat[CarAdvert] = Json.format[CarAdvert]

  val carAdvertRefinedModel: CarAdvertRefined = CarAdvertRefined(
    "Audi A4 Avant",
    Gasoline,
    2500,
    `new` = false,
    Some(85000),
    Some(LocalDate.of(2002, 1, 1))
  )

  val createdAt: LocalDateTime = LocalDateTime.of(2019, 3, 10, 12, 30, 27, 444)

  val carAdvertRefinedJSONString =
    """{"title":"Audi A4 Avant","fuel":{"_type":"models.Gasoline"},"price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01"}"""
//    """{"id":"f0eb2a01-c947-46bd-8b41-7ec80e3a50d5","title":"Audi A4 Avant","fuel":{"_type":"models.Gasoline"},"price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01"}"""

  val carAdvertRefinedJSONWrongString =
    """{"title":"Audi A4 Avant","fuel":"Gasoline","price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01"}"""
//    """{"id":"f0eb2a01-c947-46bd-8b41-7ec80e3a50d5","title":"Audi A4 Avant","fuel":"Gasoline","price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01"}"""

  val carAdvertJSONString =
    s"""{"id":1,"title":"Audi A4 Avant","fuel":"gasoline","price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01","createdAt":"$createdAt"}"""
//    s"""{"id":1,"title":"Audi A4 Avant","fuel":"gasoline","price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01","createdAt":"${removeTrailingZeroInLocalDateTimeMillis(
//      createdAt)}"}"""

  "A CarAdvertRefined model" should {
    "be parsed to JSON" in {
      assert(
        Json
          .toJson(carAdvertRefinedModel)
          === Json.parse(carAdvertRefinedJSONString))
    }
    "be converted to a CarAdvert model and be parsed to JSON as well" in {
      assert(
        Json
          .toJson(carAdvertRefinedModel.toCarAdvert(Some(1), createdAt))
          === Json.parse(carAdvertJSONString))
    }
    "be parsed to JSON from String and converted to CarAdvertRefinedModel" in {
      assert(Json.parse(carAdvertRefinedJSONString).as[CarAdvertRefined] === carAdvertRefinedModel)
    }
    "throw JsResultException if parsed and constructed from a malformed String" in {
      a[JsResultException] must be thrownBy {
        Json.parse(carAdvertRefinedJSONWrongString).as[CarAdvertRefined]
      }

    }
  }
}
