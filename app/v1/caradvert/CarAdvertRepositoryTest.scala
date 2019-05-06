package v1.caradvert

import java.time.LocalDateTime

import executioncontexts.CarAdvertExecutionContext
import javax.inject.{Inject, Singleton}
import models.CarAdvertRefined
import models.slick.CarAdvert
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future

@Singleton
class CarAdvertRepositoryTest @Inject()()(implicit ec: CarAdvertExecutionContext) extends CarAdvertRepository {

  private val logger = Logger(this.getClass)

  val createdAt: LocalDateTime = LocalDateTime.of(2019, 3, 10, 12, 30, 27, 444)

  override def create(data: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int] = {
    logger.trace("create impl: ")
    Future {
      1
    }
  }

  override def update(id: Long, data: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int] = {
    logger.trace("update impl: ")
    Future {
      1
    }
  }

  override def list(sortByField: Option[String])(implicit mc: MarkerContext): Future[Iterable[CarAdvert]] = {
    logger.trace("list impl: ")

    sortByField.getOrElse("") match {
      case "" =>
        Future {
          Seq(CarAdvert(Some(1), "Audi A4", "diesel", 3000, true, createdAt = createdAt))
        }
//      case "title"              => carAdvertDao.getAllSortedByTitle()
//      case "fuel"               => carAdvertDao.getAllSortedByFuel()
//      case "new"                => carAdvertDao.getAllSortedByIsNew()
//      case "price"              => carAdvertDao.getAllSortedByPrice()
//      case "mileage"            => carAdvertDao.getAllSortedByMileage()
//      case "first registration" => carAdvertDao.getAllSortedByFirstRegistration()
//      case "createdAt"          => carAdvertDao.getAllSortedByCreatedAt()
//      case "updatedAt"          => carAdvertDao.getAllSortedByUpdatedAt()

      case _ =>
        Future {
          Seq(CarAdvert(Some(1), "Audi A4", "diesel", 3000, true, createdAt = createdAt))
        }
    }

  }

  override def get(id: Long)(implicit mc: MarkerContext): Future[Option[CarAdvert]] = {
    logger.trace("get impl: ")
    Future {
      Some(CarAdvert(Some(1), "Audi A4", "diesel", 3000, true, createdAt = createdAt))
    }
  }

  override def delete(id: Long)(implicit mc: MarkerContext): Future[Int] = {
    logger.trace("delete impl: ")
    Future {
      1
    }
  }
}
