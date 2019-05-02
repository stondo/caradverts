package v1.caradvert

import java.util.UUID

import models.CarAdvert
import play.api.MarkerContext

import scala.concurrent.Future

trait CarAdvertRepository {
  def create(data: CarAdvert)(implicit mc: MarkerContext): Future[Option[UUID]]

  def update(data: CarAdvert)(implicit mc: MarkerContext): Future[Boolean] // Future[Option[CarAdvert]]

  def list()(implicit mc: MarkerContext): Future[Iterable[CarAdvert]]

  def get(id: UUID)(implicit mc: MarkerContext): Future[Option[CarAdvert]]

  def delete(id: UUID)(implicit mc: MarkerContext): Future[Boolean]
}
