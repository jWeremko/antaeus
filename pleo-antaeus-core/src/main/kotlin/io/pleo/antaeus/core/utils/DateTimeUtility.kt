package io.pleo.antaeus.core.utils

import java.util.*

interface DateTimeUtility {
    fun currentMonthFirstDay(now : Date) : Date

    fun nextMonthFirstDay(now : Date) : Date

    fun nextMonthFirstDayDelay(now : Date) : Long

    fun now() : Date
}


