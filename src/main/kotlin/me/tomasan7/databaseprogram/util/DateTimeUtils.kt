package me.tomasan7.databaseprogram.util

import kotlinx.datetime.*
import java.time.format.DateTimeFormatter
import java.time.LocalDate as JLocalDate

fun LocalDate.Companion.now(): LocalDate
{
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

fun LocalDate.format(formatter: DateTimeFormatter): String
{
    return this.toJavaLocalDate().format(formatter)
}

fun String.parseLocalDate(formatter: DateTimeFormatter): LocalDate
{
    return JLocalDate.parse(this, formatter).toKotlinLocalDate()
}
