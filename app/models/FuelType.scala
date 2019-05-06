package models

import play.api.libs.json.{JsError, JsObject, JsSuccess, Json, OFormat, OWrites, Reads}

sealed abstract class FuelType
case object Gasoline extends FuelType
case object Diesel   extends FuelType
//case object NotSupported extends FuelType

object FuelType {

  val fuelTypeMap: Map[Option[String], Option[FuelType]] =
    Map(Some("gasoline") -> Some(Gasoline), Some("diesel") -> Some(Diesel), None -> None)

  implicit val gaolineFormat: OFormat[Gasoline.type] = OFormat[Gasoline.type](Reads[Gasoline.type] {
    case JsObject(_) => JsSuccess(Gasoline)
    case _           => JsError("Empty object expected")
  }, OWrites[Gasoline.type] { _ =>
    Json.obj()
  })

  implicit val dieselFormat: OFormat[Diesel.type] = OFormat[Diesel.type](Reads[Diesel.type] {
    case JsObject(_) => JsSuccess(Diesel)
    case _           => JsError("Empty object expected")
  }, OWrites[Diesel.type] { _ =>
    Json.obj()
  })

//  implicit val notSupportedFormat: OFormat[NotSupported.type] = OFormat[NotSupported.type](Reads[NotSupported.type] {
//    case JsObject(_) => JsSuccess(NotSupported)
//    case _           => JsError("Empty object expected")
//  }, OWrites[NotSupported.type] { _ =>
//    Json.obj()
//  })

  implicit val fuelTypeFormat: OFormat[FuelType] = Json.format[FuelType]
}
