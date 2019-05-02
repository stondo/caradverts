package utils

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.numeric.PosInt

object Refinements {
  type Title   = Refined[String, NonEmpty]
  type Price   = PosInt
  type Mileage = PosInt
}
