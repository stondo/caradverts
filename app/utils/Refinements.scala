package utils

import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import de.swsnr.refined.play.json._

object Refinements {

  type Title = Refined[String, NonEmpty]
  object Title extends RefinedTypeOps[Title, String]

  type Price = PosInt
  object Price extends RefinedTypeOps[Price, Int]

  type Mileage = PosInt
  object Mileage extends RefinedTypeOps[Mileage, Int]

}
