import models.slick.CarAdvert
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.Application
import play.api.http.HeaderNames
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Result
import play.api.test.Helpers.{GET, HOST, contentAsJson, route, status, _}
import play.api.test.{FakeHeaders, FakeRequest}

import scala.concurrent.Future

object InMemoryDatabaseConf {
  val inMemoryDatabaseConfiguration: Map[String, Any] = Map(
    "slick.dbs.default.profile"     -> "slick.jdbc.H2Profile$",
    "slick.dbs.default.driver"      -> "slick.driver.H2Driver$",
    "slick.dbs.default.db.driver"   -> "org.h2.Driver",
    "slick.dbs.default.db.url"      -> "jdbc:h2:mem:play;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=FALSE",
    "slick.dbs.default.db.user"     -> "",
    "slick.dbs.default.db.password" -> ""
  )

}

class CarAdvertRouterSpec extends PlaySpec with GuiceOneAppPerTest {

  implicit val carAdvertFormat: OFormat[CarAdvert] = Json.format[CarAdvert]

  import InMemoryDatabaseConf._

  val prefix = "/v1/caradverts"

  val requestHeaders = FakeHeaders(List(HOST -> "localhost:9000", HeaderNames.CONTENT_TYPE -> "application/json"))

  override def fakeApplication(): Application = {

    GuiceApplicationBuilder()
//      .overrides(bind[CarAdvertRepository].to[CarAdvertRepositoryTest].in[Singleton])
      .configure(inMemoryDatabaseConfiguration)
      .build()
  }

  "CarAdvertRouter" should {

    "create 2 car adverts" in {

      val carAdvertUsedMap = Map("title" -> "Audi A4 Avant",
                                 "fuel"               -> "gasoline",
                                 "price"              -> "2500",
                                 "new"                -> "false",
                                 "mileage"            -> "85000",
                                 "first registration" -> "2002-01-01")

      val carAdvertNewMap = Map("title" -> "BMW", "fuel" -> "diesel", "price" -> "5500", "new" -> "true")

      val bodyUsedCar = Json.toJson(carAdvertUsedMap)

      val bodyNewCar = Json.toJson(carAdvertNewMap)

      val createUsedCarRequest =
        FakeRequest(
          POST,
          prefix,
          requestHeaders,
          bodyUsedCar
        )

      val createNewCarRequest =
        FakeRequest(
          POST,
          prefix,
          requestHeaders,
          bodyNewCar
        )

      val createUsedCar: Future[Result] = route(fakeApplication(), createUsedCarRequest).get

      val createNewCar: Future[Result] = route(fakeApplication(), createNewCarRequest).get

      status(createUsedCar) mustBe CREATED

      status(createNewCar) mustBe CREATED

    }
    "the list of returned car adverts has size 2" in {
      val listCarAdvertsReuqest = FakeRequest(GET, prefix).withHeaders(HOST -> "localhost:9000")

      val index: Future[Result] = route(fakeApplication(), listCarAdvertsReuqest).get

      val caradverts: Seq[CarAdvert] = Json.fromJson[Seq[CarAdvert]](contentAsJson(index)).get

//      caradverts.foreach(println)

      caradverts.size mustBe 2
      caradverts.head.price mustBe 2500
      caradverts.reverse.head.price mustBe 5500
    }
    "the list of returned car adverts sorted by price" in {
      val listCarAdvertsReuqest = FakeRequest(GET, prefix + "?sortBy=price").withHeaders(HOST -> "localhost:9000")

      val index: Future[Result] = route(fakeApplication(), listCarAdvertsReuqest).get

      val caradverts: Seq[CarAdvert] = Json.fromJson[Seq[CarAdvert]](contentAsJson(index)).get

      //      caradverts.foreach(println)

      caradverts.head.price mustBe 5500
      caradverts.reverse.head.price mustBe 2500
    }
    "update car advert with id 2 to have a price of 65000" in {

      val carAdvertUpdateNewMap = Map("title" -> "BMW", "fuel" -> "diesel", "price" -> "65000", "new" -> "true")

      val bodyUpdateNewCar = Json.toJson(carAdvertUpdateNewMap)

      val updateNewCarRequest =
        FakeRequest(
          PUT,
          prefix + "/2",
          requestHeaders,
          bodyUpdateNewCar
        )

      val update: Future[Result] = route(fakeApplication(), updateNewCarRequest).get

      status(update) mustBe NO_CONTENT
    }
    "get the car advert with id 2 and check that it has been updated to have a price of 65000" in {
      val showCarAdvertsReuqest = FakeRequest(GET, prefix + "/2").withHeaders(HOST -> "localhost:9000")

      val index: Future[Result] = route(fakeApplication(), showCarAdvertsReuqest).get

      val caradvert: CarAdvert = Json.fromJson[CarAdvert](contentAsJson(index)).get

//      print(caradvert)

      caradvert.price mustBe 65000
    }
    "delete car advert with id 1" in {
      val deleteUsedCarRequest =
        FakeRequest(
          DELETE,
          prefix + "/1"
        )

      val delete: Future[Result] = route(fakeApplication(), deleteUsedCarRequest).get

      status(delete) mustBe NO_CONTENT
    }
    "check that the car advert with id 1 has been deleted" in {
      val showCarAdvertsReuqest = FakeRequest(GET, prefix + "/1").withHeaders(HOST -> "localhost:9000")

      val index: Future[Result] = route(fakeApplication(), showCarAdvertsReuqest).get

      status(index) mustBe NOT_FOUND

    }

  }

}
