package models.slick.daos

import java.time.{LocalDate, LocalDateTime}

import javax.inject.Inject
import models.slick.CarAdvert
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{JdbcProfile, PostgresProfile}
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}

class CarAdvertDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
//    extends HasDatabaseConfigProvider[PostgresProfile] {
  import profile.api._

  class CarAdverts(tag: Tag) extends Table[CarAdvert](tag, "CARADVERT") {

    def id                   = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def title                = column[String]("title")
    def fuel                 = column[String]("fuel")
    def price                = column[Int]("price")
    def `new`                = column[Boolean]("new")
    def mileage              = column[Option[Int]]("mileage")
    def `first registration` = column[Option[LocalDate]]("first registration")
    def createdAt            = column[LocalDateTime]("createdAt")
    def updatedAt            = column[Option[LocalDateTime]]("updatedAt")

    def * : ProvenShape[CarAdvert] =
      (id.?, title, fuel, price, `new`, mileage, `first registration`, createdAt, updatedAt) <> (CarAdvert.tupled, CarAdvert.unapply)
  }

  private val caradverts = TableQuery[CarAdverts]

  /** Count all caradverts. */
  def count(): Future[Int] =
    db.run(caradverts.length.result)

  /** List all caradverts. */
  def getAll(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.id.desc).result)

  def getAllSortedByTitle(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.title.desc.nullsLast).result)

  def getAllSortedByFuel(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.fuel.desc.nullsLast).result)

  def getAllSortedByIsNew(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.`new`.desc.nullsLast).result)

  def getAllSortedByMileage(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.mileage.desc.nullsLast).result)

  def getAllSortedByPrice(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.price.desc.nullsLast).result)

  def getAllSortedByFirstRegistration(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.`first registration`.desc.nullsLast).result)

  def getAllSortedByCreatedAt(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.createdAt.desc.nullsLast).result)

  def getAllSortedByUpdatedAt(): Future[Seq[CarAdvert]] =
    db.run(caradverts.sortBy(_.updatedAt.desc.nullsLast).result)

  /** Get caradvert by id. */
  def getById(id: Long): Future[Option[CarAdvert]] = {
    db.run(caradverts.filter(_.id === id).result.headOption)
  }

  /** Insert a caradvert. */
  def insert(caradvert: CarAdvert): Future[Int] =
    db.run(caradverts += caradvert)

  /** Update a caradvert. */
  def update(id: Long, caradvert: CarAdvert): Future[Int] = {
    db.run(caradverts.filter(_.id === id).update(caradvert))
  }

  /** Delete a caradvert. */
  def delete(id: Long): Future[Int] =
    db.run(caradverts.filter(_.id === id).delete)
}
