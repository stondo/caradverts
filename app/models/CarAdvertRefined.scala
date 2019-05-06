package models

import java.time.{LocalDate, LocalDateTime}

import play.api.libs.json.{Json, OFormat}
import utils.Refinements.{Mileage, Price, Title}
import de.swsnr.refined.play.json._
import models.slick.CarAdvert

/*
Car adverts should have the following fields:

 id (required): int or guid, choose whatever is more convenient for you;
 title (required): string, e.g. "Audi A4 Avant";
 fuel (required): gasoline or diesel, use some type which could be extended in the future by adding additional fuel types;
 price (required): integer;
 new (required): boolean, indicates if car is new or used;
 mileage (only for used cars): integer;
 first registration (only for used cars):date without time.
 */

final case class CarAdvertRefined(title: Title,
                                  fuel: FuelType,
                                  price: Price,
                                  `new`: Boolean,
                                  mileage: Option[Mileage] = None,
                                  `first registration`: Option[LocalDate] = None) {

  def toCarAdvert(id: Option[Long] = None,
                  createdAt: LocalDateTime = LocalDateTime.now,
                  updatedAt: Option[LocalDateTime] = None) =
    CarAdvert(
      id,
      title.value,
      fuel.toString.toLowerCase,
      price.value,
      `new`,
      if (mileage.nonEmpty) Some(mileage.get.value) else None,
      if (`first registration`.nonEmpty) Some(`first registration`.get) else None,
      createdAt,
      updatedAt
    )
}

object CarAdvertRefined {
  implicit val carAdvertRefinedFormat: OFormat[CarAdvertRefined] = Json.format[CarAdvertRefined]
}
