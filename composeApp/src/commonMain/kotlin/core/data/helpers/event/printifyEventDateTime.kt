package core.data.helpers.event

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun printifyEventDateTime(date: Instant): String {
    val d = date.toLocalDateTime(TimeZone.UTC)

    return "${d.dayOfMonth}. ${d.monthNumber}. ${d.year}, ${d.hour}:${d.minute}"
}