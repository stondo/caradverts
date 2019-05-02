package models

import play.api.libs.json.{JsError, JsObject, JsSuccess, Json, OFormat, OWrites, Reads}

sealed abstract class FuelType
case object Gasoline    extends FuelType
case object Diesel      extends FuelType
case object NotSelected extends FuelType

object FuelType {

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

  implicit val notSelectedFormat: OFormat[NotSelected.type] = OFormat[NotSelected.type](Reads[NotSelected.type] {
    case JsObject(_) => JsSuccess(NotSelected)
    case _           => JsError("Empty object expected")
  }, OWrites[NotSelected.type] { _ =>
    Json.obj()
  })

  implicit val fuelTypeFormat: OFormat[FuelType] = Json.format[FuelType]
}
