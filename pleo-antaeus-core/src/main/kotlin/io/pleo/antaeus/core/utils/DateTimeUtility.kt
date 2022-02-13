package io.pleo.antaeus.core.utils

import java.util.*

interface DateTimeUtility {
    fun currentMonthFirstDay() : Date

    fun nextMonthFirstDay() : Date

    fun nextMonthFirstDay(now : Date) : Long

    fun now() : Date
}


