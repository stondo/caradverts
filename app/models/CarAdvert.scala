package models

import java.time.LocalDate
import java.util.UUID
import play.api.libs.json.{Json, OFormat}
import utils.Refinements.{Mileage, Price, Title}
import de.swsnr.refined.play.json._

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

final case class CarAdvert(id: UUID,
                           title: Title,
                           fuel: FuelType,
                           price: Price,
                           `new`: Boolean,
                           mileage: Option[Mileage] = None,
                           `first registration`: Option[LocalDate] = None)

object CarAdvert {
  implicit val carAdvertFormat: OFormat[CarAdvert] = Json.format[CarAdvert]
}
