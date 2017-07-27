package com.wework.love

import java.util.Calendar
import java.util.Date

internal object RevenueCalculator {

  fun calculateRevenue(inputMonth: Date, reservations: List<OfficeReservation>) {
    val calendar = Calendar.getInstance()
    calendar.time = inputMonth
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH).toLong()
    val millisecondsInMonth = daysInMonth * 24 * 60 * 60 * 1000
    var revenue = 0
    var unreservedCapacity = 0
    for (reservation in reservations) {
      val startTime = reservation.startDay!!.time
      val endOfInputMonthTime = inputMonth.time + millisecondsInMonth - 1

      //if start time of reservation is within given month - start calculation
      //otherwise stop calculation. the list is sorted by date therefore all rest list will not be within month at all
      if (startTime <= endOfInputMonthTime && (reservation.endDay == null || reservation.endDay.time >= inputMonth.time)) {

        val startDay = getStartDay(inputMonth, reservation.startDay, startTime)
        val endDay = getEndDay(inputMonth, daysInMonth, reservation.endDay, endOfInputMonthTime)

        val prorata: Float = if (endDay - startDay >= 0) (endDay - startDay).toFloat() / daysInMonth else 0f
        revenue += Math.round(prorata * reservation.monthlyPrice)
      } else {
        unreservedCapacity += reservation.capacity
      }
    }
    println(String.format(
        "expected revenue: $%d, expected total capacity of the unreserved offices: %d\n", revenue,
        unreservedCapacity))
  }

  /**
   * Calculate what day is the end of the moth end day not exists than number of days in month is
   * returned end day exists and it's before input month then is return (just in case someone forgot
   * to call validate on data set) end day exists and it's within input month then day in month is
   * returned
   */
  private fun getEndDay(month: Date, daysInMonth: Long, endDate: Date?,
                        endOfMonthTime: Long): Long {
    if (endDate == null || endDate.time > endOfMonthTime) {
      return daysInMonth
    } else if (endDate.time < month.time) {
      return 0
    } else {
      val cal = Calendar.getInstance()
      cal.time = endDate
      return cal.get(Calendar.DAY_OF_MONTH).toLong()
    }
  }

  /**
   * Calculate what is start day of reservation to calculate.If start day is before the input , then
   * start day is 0. If start day is after the input , then start day is the day itself

   * @param month - input month
   * *
   * @param startDate date of office reservation starts
   * *
   * @param startTime start time of reserviation
   * *
   * @return day when reservation begins in month
   */
  private fun getStartDay(month: Date, startDate: Date, startTime: Long): Long {
    if (startTime <= month.time) {
      return 0
    } else {
      val cal = Calendar.getInstance()
      cal.time = startDate
      return (cal.get(Calendar.DAY_OF_MONTH) - 1).toLong() //-1 to include the day reservation starts
    }
  }
}