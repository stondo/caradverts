package models.validation

import java.time.LocalDate

final case class CarAdvertFormInput(title: String,
                                    fuel: String,
                                    price: Int,
                                    `new`: Boolean,
                                    mileage: Option[Int] = None,
                                    `first registration`: Option[LocalDate] = None)
