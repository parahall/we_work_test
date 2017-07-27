package com.wework.love

import com.opencsv.bean.CsvToBeanBuilder
import com.sun.media.sound.InvalidDataException
import java.io.FileNotFoundException
import java.io.FileReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.InputMismatchException
import java.util.Scanner

object Main {

  @Throws(InvalidDataException::class)
  @JvmStatic fun main(args: Array<String>) {
    if (args.size != 1) {
      throw IllegalArgumentException(
          "Wrong usage. Please provide data set file as first argument")
    }
    val fileReader: FileReader?
    try {
      fileReader = FileReader(args[0])
    } catch (e: FileNotFoundException) {
      throw IllegalArgumentException(
          String.format("Wrong data set file. Unable open %s for reading", args[0]))
    }

    @Suppress("UNCHECKED_CAST")
    val reservations: List<OfficeReservation> = CsvToBeanBuilder<OfficeReservation>(fileReader).withIgnoreLeadingWhiteSpace(true)
        .withSkipLines(1)
        .withType(OfficeReservation::class.java)
        .build()
        .parse() as List<OfficeReservation>

    DataSetValidator.validateDataSet(reservations)
    println("Data set loaded correctly." + System.getProperty("line.separator"))

    var input: String
    do {
      println(
          "Enter month to calculate revenue in following format YYYY-mm or exit to finish:")
      val scanner = Scanner(System.`in`)
      input = scanner.nextLine()
      if (!input.equals("Exit", ignoreCase = true)) {
        try {
          val date = parseInput(input)
          RevenueCalculator.calculateRevenue(date, reservations)
        } catch (e: InputMismatchException) {
          println("Invalid input. Unable to parse it")
        }

      }
    } while (!input.equals("Exit", ignoreCase = true))
  }

  private fun parseInput(input: String): Date {
    val inputDate: Date
    if (input.matches("\\d{4}-\\d{2}".toRegex())) {

      try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM")
        inputDate = simpleDateFormat.parse(input)
      } catch (e: ParseException) {
        throw InputMismatchException("Invalid input. Unable to parse it")
      }

      return inputDate
    } else {
      throw InputMismatchException("Invalid input. Unable to parse it")
    }
  }
}
