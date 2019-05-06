package v1.caradvert

import java.time.LocalDateTime
import java.util.UUID

import executioncontexts.CarAdvertExecutionContext
import javax.inject.{Inject, Singleton}
import models.CarAdvertRefined
import models.slick.CarAdvert
import models.slick.daos.CarAdvertDAO
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future

@Singleton
class CarAdvertRepositoryImpl @Inject()(carAdvertDao: CarAdvertDAO)(implicit ec: CarAdvertExecutionContext)
    extends CarAdvertRepository {

  private val logger = Logger(this.getClass)

  override def create(data: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int] = {
    logger.trace("create impl: ")
    carAdvertDao.insert(data.toCarAdvert())
  }

  override def update(id: Long, data: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int] = {
    logger.trace("update impl: ")
    carAdvertDao.update(id, data.toCarAdvert(Some(id), updatedAt = Some(LocalDateTime.now)))
  }

  override def list(sortByField: Option[String])(implicit mc: MarkerContext): Future[Iterable[CarAdvert]] = {
    logger.trace("list impl: ")

    sortByField.getOrElse("") match {
      case ""                   => carAdvertDao.getAll()
      case "title"              => carAdvertDao.getAllSortedByTitle()
      case "fuel"               => carAdvertDao.getAllSortedByFuel()
      case "new"                => carAdvertDao.getAllSortedByIsNew()
      case "price"              => carAdvertDao.getAllSortedByPrice()
      case "mileage"            => carAdvertDao.getAllSortedByMileage()
      case "first registration" => carAdvertDao.getAllSortedByFirstRegistration()
      case "createdAt"          => carAdvertDao.getAllSortedByCreatedAt()
      case "updatedAt"          => carAdvertDao.getAllSortedByUpdatedAt()

      case _ => carAdvertDao.getAll()
    }

  }

  override def get(id: Long)(implicit mc: MarkerContext): Future[Option[CarAdvert]] = {
    logger.trace("get impl: ")
    carAdvertDao.getById(id)
  }

  override def delete(id: Long)(implicit mc: MarkerContext): Future[Int] = {
    logger.trace("delete impl: ")
    carAdvertDao.delete(id)
  }
}
