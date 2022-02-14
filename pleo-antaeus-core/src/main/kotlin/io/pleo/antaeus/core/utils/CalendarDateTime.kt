package io.pleo.antaeus.core.utils

import java.util.*

class CalendarDateTime : DateTimeUtility {

    override fun currentMonthFirstDay(now : Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = now
        resetCalendarTime(calendar)
        return calendar.time
    }

    override fun nextMonthFirstDay(now : Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = now
        resetCalendarTime(calendar)
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1)
        return calendar.time
    }

    override fun nextMonthFirstDayDelay(now : Date): Long {
        return nextMonthFirstDay(now).time - now.time
    }

    override fun now(): Date {
        val cal = Calendar.getInstance()
        return cal.time
    }

    private fun resetCalendarTime(calendar: Calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
}