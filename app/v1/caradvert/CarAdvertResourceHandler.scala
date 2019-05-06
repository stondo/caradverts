package v1.caradvert

import javax.inject.{Inject, Provider}
import models.CarAdvertRefined
import models.slick.CarAdvert
import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}

/**
  * Controls access to the backend data, returning [[CarAdvert]]
  */
class CarAdvertResourceHandler @Inject()(routerProvider: Provider[CarAdvertRouter],
                                         carAdvertRepository: CarAdvertRepository)(implicit ec: ExecutionContext) {

  def create(carAdvert: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int] = {
    carAdvertRepository.create(carAdvert)
  }

  def lookup(id: Long)(implicit mc: MarkerContext): Future[Option[CarAdvert]] = {
    carAdvertRepository.get(id)
  }

  def find(sortByField: Option[String])(implicit mc: MarkerContext): Future[Iterable[CarAdvert]] = {
    carAdvertRepository.list(sortByField)
  }

  def update(id: Long, carAdvertRefined: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int] = {
    carAdvertRepository.update(id, carAdvertRefined)
  }

  def delete(id: Long)(implicit mc: MarkerContext): Future[Int] = {
    carAdvertRepository.delete(id)
  }

}
