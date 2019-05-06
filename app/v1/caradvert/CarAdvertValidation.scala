package v1.caradvert

import java.time.LocalDate

import javax.inject.Inject
import models.validation.CarAdvertFormInput
import models.{CarAdvertRefined, FuelType}
import play.api.Configuration
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import utils.Refinements.{Mileage, Price, Title}

import scala.util.matching.Regex

class CarAdvertValidation @Inject()(configuration: Configuration) {

  private val allowedFuelTypes: String = configuration.get[String]("fuelTypesRegEx")

  private val allowedFuelTypesRe: Regex = allowedFuelTypes.r

  private val fuelTypeCheckConstraint: Constraint[String] = Constraint("constraints.fueltype")({ plainText =>
    val errors = plainText match {
      case allowedFuelTypesRe() => Nil
      case _                    => Seq(ValidationError(s"Fuel can only be of these types: ${allowedFuelTypes.split('|').mkString(", ")}"))
    }
    if (errors.isEmpty) {
      Valid
    } else {
      Invalid(errors)
    }
  })

  private val newCarAdvertConstraint: Constraint[CarAdvertFormInput] = Constraint("constraints.newcaradvert")({ form =>
    val errors = form match {
      case f =>
        if (f.`new` && f.mileage.nonEmpty && f.`first registration`.nonEmpty)
          Seq(ValidationError("New car advert are not allowed to have mileage and first registration"))
        else if (f.`new` && f.mileage.nonEmpty) Seq(ValidationError("New car advert are not allowed to have mileage"))
        else if (f.`new` && f.`first registration`.nonEmpty)
          Seq(ValidationError("New car advert are not allowed to have first registration"))
        else Nil
    }
    if (errors.isEmpty) {
      Valid
    } else {
      Invalid(errors)
    }
  })

  private val usedCarAdvertConstraint: Constraint[CarAdvertFormInput] = Constraint("constraints.usedcaradvert")({
    form =>
      val errors = form match {
        case f =>
          if (!f.`new` && f.mileage.isEmpty && f.`first registration`.isEmpty)
            Seq(ValidationError("Used car advert must have mileage and first registration"))
          else if (!f.`new` && f.mileage.isEmpty) Seq(ValidationError("Used car advert must have mileage"))
          else if (!f.`new` && f.`first registration`.isEmpty)
            Seq(ValidationError("Used car advert must have first registration"))
          else Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })

  val form: Form[CarAdvertFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "title" -> nonEmptyText,
        //        "fuel"               -> of[FuelType],
        "fuel"    -> nonEmptyText.verifying(fuelTypeCheckConstraint),
        "price"   -> number(min = 1),
        "new"     -> boolean,
        "mileage" -> optional(number(min = 1)),
        "first registration" -> optional(localDate.verifying("format: yyyy-MM-dd", {
          _.isAfter(LocalDate.of(1903, 1, 1))
        }))
      )(CarAdvertFormInput.apply)(CarAdvertFormInput.unapply)
        .verifying(newCarAdvertConstraint)
        .verifying(usedCarAdvertConstraint)
    )
  }

  def toCarAdvertRefined(carAdvertFormInput: CarAdvertFormInput): CarAdvertRefined = {

    val titleRefinedEither: Either[String, Title] = Title.from(carAdvertFormInput.title)
    val fuelRefinedEither: Option[FuelType] =
      FuelType.fuelTypeMap(
        allowedFuelTypes
          .split('|')
          .find(_ == carAdvertFormInput.fuel))

    val priceRefinedEither: Either[String, Price] = Price.from(carAdvertFormInput.price)

    carAdvertFormInput.mileage.fold {
      val (titleRefined, fuelRefined, priceRefined) =
        if (titleRefinedEither.isRight && fuelRefinedEither.nonEmpty && priceRefinedEither.isRight)
          (titleRefinedEither.right.get, fuelRefinedEither.get, priceRefinedEither.right.get)
        else throw new Exception("Invalid starting value provided")

      CarAdvertRefined(titleRefined, fuelRefined, priceRefined, carAdvertFormInput.`new`)

    } { mil =>
      val mileageRefinedEither = Mileage.from(mil)
      val (titleRefined, fuelRefined, priceRefined, mileageRefined) =
        if (titleRefinedEither.isRight && fuelRefinedEither.nonEmpty && priceRefinedEither.isRight && mileageRefinedEither.isRight)
          (titleRefinedEither.right.get,
           fuelRefinedEither.get,
           priceRefinedEither.right.get,
           mileageRefinedEither.right.get)
        else throw new Exception("Invalid starting value provided")

      CarAdvertRefined(titleRefined,
                       fuelRefined,
                       priceRefined,
                       carAdvertFormInput.`new`,
                       Some(mileageRefined),
                       carAdvertFormInput.`first registration`)
    }
  }
}
