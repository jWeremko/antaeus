package io.pleo.antaeus.core.utils

import java.util.*

class CalendarDateTime : DateTimeUtility {

    override fun currentMonthFirstDay(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        return cal.time
    }

    override fun nextMonthFirstDay(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1)
        return cal.time
    }

    override fun nextMonthFirstDay(now : Date): Long {
        return nextMonthFirstDay().time - now.time
    }

    override fun now(): Date {
        val cal = Calendar.getInstance()
        return cal.time
    }
}