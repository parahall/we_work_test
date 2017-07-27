package com.wework.love

import com.sun.media.sound.InvalidDataException

object DataSetValidator {
  @Throws(InvalidDataException::class)
  internal fun validateDataSet(reservations: List<OfficeReservation>) {
    for (reservation in reservations) {
      validateStartEndDate(reservation)
      validateCapacity(reservation)
      validatePrice(reservation)
    }
  }

  @Throws(InvalidDataException::class)
  internal fun validatePrice(reservation: OfficeReservation) {
    if (reservation.monthlyPrice < 0) {
      throw InvalidDataException(
          String.format(
              "Invalid data set. The monthly prices can't be less then zero %sData: %s",
              System.getProperty("line.separator"), reservation.toString()))
    }
  }

  @Throws(InvalidDataException::class)
  internal fun validateCapacity(reservation: OfficeReservation) {
    if (reservation.capacity <= 0) {
      throw InvalidDataException(
          String.format(
              "Invalid data set. The capacity can't be less or equal to zero %sData: %s",
              System.getProperty("line.separator"), reservation.toString()))
    }
  }

  @Throws(InvalidDataException::class)
  internal fun validateStartEndDate(reservation: OfficeReservation) {
    if (reservation.endDay != null && reservation.startDay!!.time > reservation.endDay.time) {
      throw InvalidDataException(
          String.format("Invalid data set. The start day is great then end date.%sData: %s",
              System.getProperty("line.separator"), reservation.toString()))
    }
  }
}