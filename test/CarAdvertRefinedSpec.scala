import java.time.LocalDate
import java.util.UUID

import models.{CarAdvert, Gasoline}
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.{JsResultException, Json}
import eu.timepit.refined.auto._

class CarAdvertSpec extends PlaySpec {

  val carAdvertModel: CarAdvert = CarAdvert(
    UUID.fromString("f0eb2a01-c947-46bd-8b41-7ec80e3a50d5"),
    "Audi A4 Avant",
    Gasoline,
    2500,
    `new` = false,
    Some(85000),
    Some(LocalDate.of(2002, 1, 1))
  )

  val carAdvertJSONString =
    """{"id":"f0eb2a01-c947-46bd-8b41-7ec80e3a50d5","title":"Audi A4 Avant","fuel":{"_type":"models.Gasoline"},"price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01"}"""

  val carAdvertJSONWrongString =
    """{"id":"f0eb2a01-c947-46bd-8b41-7ec80e3a50d5","title":"Audi A4 Avant","fuel":"Gasoline","price":2500,"new":false,"mileage":85000,"first registration":"2002-01-01"}"""

  "A CarAdvert model" should {
    "be parsable to JSON" in {
      assert(
        Json
          .toJson(carAdvertModel)
          .toString() === carAdvertJSONString)
    }
    "be parsable from String" in {
      assert(Json.parse(carAdvertJSONString).as[CarAdvert] === carAdvertModel)
    }
    "throw JsResultException if parsed and constructed from a malformed String" in {
      a[JsResultException] must be thrownBy {
        Json.parse(carAdvertJSONWrongString).as[CarAdvert]
      }

    }
  }
}
