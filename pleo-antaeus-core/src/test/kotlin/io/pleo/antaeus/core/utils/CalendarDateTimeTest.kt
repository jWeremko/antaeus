package io.pleo.antaeus.core.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class CalendarDateTimeTest {
    private val calendar = mockk<Calendar>()
    private val calendarDateTime = CalendarDateTime()

    private val expectedDate = Date.from(LocalDate
            .of(2020, 2, 1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant())


    @Test
    fun `should return current month first day`() {
        //TODO: this test is a bit artificial, think how to make it better
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        every { calendar.set(any(), any()) } returns Unit
        every { calendar.time } returns expectedDate

        assertEquals(expectedDate, calendarDateTime.currentMonthFirstDay())
    }

    @Test
    fun `should return next month first day`() {
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        every { calendar.get(Calendar.MONTH) } returns 1
        every { calendar.set(any(), any()) } returns Unit
        every { calendar.time } returns expectedDate

        assertEquals(expectedDate, calendarDateTime.nextMonthFirstDay())
    }

    @Test
    fun `should return milliseconds to the next month first day`() {
        val now = Date(expectedDate.time - 100)

        mockkStatic(Calendar::class)
        every { Calendar.getInstance() } returns calendar
        every { calendar.set(any(), any()) } returns Unit
        every { calendar.get(Calendar.MONTH) } returns 1
        every { calendar.time } returns expectedDate

        assertEquals(100, calendarDateTime.nextMonthFirstDay(now))
    }
}