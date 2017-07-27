package com.wework.love

import com.opencsv.bean.CsvBindByPosition
import com.opencsv.bean.CsvDate
import java.util.Date

class OfficeReservation : Comparable<OfficeReservation> {

  @CsvBindByPosition(position = 0, required = true)
  internal val capacity: Int = 0

  @CsvBindByPosition(position = 1, required = true)
  internal val monthlyPrice: Int = 0

  @CsvBindByPosition(position = 2, required = true)
  @CsvDate("yyyy-MM-dd")
  internal val startDay: Date? = null

  @CsvBindByPosition(position = 3)
  @CsvDate("yyyy-MM-dd")
  internal val endDay: Date? = null

  override fun toString(): String {
    return "OfficeReservation{" +
        "capacity=" + capacity +
        ", monthly_price=" + monthlyPrice +
        ", start_day=" + startDay +
        ", end_day=" + endDay +
        '}'
  }

  override fun compareTo(other: OfficeReservation): Int {
    return java.lang.Long.compare(this.startDay!!.time, other.startDay!!.time)
  }
}
