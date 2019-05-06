package v1.caradvert

import models.CarAdvertRefined
import models.slick.CarAdvert
import play.api.MarkerContext

import scala.concurrent.Future

trait CarAdvertRepository {
  def create(data: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int]

  def update(id: Long, data: CarAdvertRefined)(implicit mc: MarkerContext): Future[Int]

  def list(sortByField: Option[String])(implicit mc: MarkerContext): Future[Iterable[CarAdvert]]

  def get(id: Long)(implicit mc: MarkerContext): Future[Option[CarAdvert]]

  def delete(id: Long)(implicit mc: MarkerContext): Future[Int]
}
