package models.slick

import java.time.{LocalDate, LocalDateTime}

final case class CarAdvert(id: Option[Long],
                           title: String,
                           fuel: String,
                           price: Int,
                           `new`: Boolean,
                           mileage: Option[Int] = None,
                           `first registration`: Option[LocalDate] = None,
                           createdAt: LocalDateTime = LocalDateTime.now,
                           updatedAt: Option[LocalDateTime] = None)
