package io.pleo.antaeus.core.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import java.util.stream.Stream


class CalendarDateTimeTest {
    private val calendarDateTime = CalendarDateTime()

    private companion object {
        @JvmStatic
        fun dates(): Stream<Arguments> = Stream.of(
                Arguments.of(LocalDate.of(2020, 2, 1).atTime(10, 10, 10)),
                Arguments.of(LocalDate.of(2020, 2, 14).atTime(10, 10, 10))
        )
    }

    private fun convertToDate(date : LocalDateTime) : Date {
        return Date.from(date.toInstant(ZoneOffset.UTC))
    }

    @ParameterizedTest
    @MethodSource("dates")
    fun `given ${0} date, should return month first day`(localDate: LocalDateTime) {
        val date = convertToDate(localDate)
        val expected = Date.from(LocalDate
                .of(2020, 2, 1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant())

        assertEquals(expected, calendarDateTime.currentMonthFirstDay(date))
    }

    @ParameterizedTest
    @MethodSource("dates")
    fun `given ${0} date, should return next month first day`(localDate: LocalDateTime) {
        val date = convertToDate(localDate)
        val expected = Date.from(LocalDate
                .of(2020, 3, 1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant())
        assertEquals(expected, calendarDateTime.nextMonthFirstDay(date))
    }

    @ParameterizedTest
    @MethodSource("dates")
    fun `given ${0} date, should return milliseconds to the next month first day`(localDate: LocalDateTime) {
        val date = convertToDate(localDate)
        val now = Date(date.time - 100)
        assertEquals(100, calendarDateTime.nextMonthFirstDayDelay(now) - calendarDateTime.nextMonthFirstDayDelay(date))
    }
}