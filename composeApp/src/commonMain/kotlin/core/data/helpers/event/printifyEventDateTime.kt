package core.data.helpers.event

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun printifyEventDateTime(date: Instant): String {
    val d = date.toLocalDateTime(TimeZone.UTC)

    return "${d.dayOfMonth}. ${d.monthNumber}. ${d.year}, ${d.hour}:${d.minute}"
}

fun printifyEventDateTime(date: String): String {
    val d = Instant.parse(date).toLocalDateTime(TimeZone.UTC)

    return "${d.dayOfMonth}. ${d.monthNumber}. ${d.year}, ${d.hour}:${d.minute}"
}

fun printifyEventDateTime(date: LocalDate?, time: LocalTime?): String {
    return if (date != null && time != null) {
        "${date.dayOfMonth}. ${date.monthNumber}. ${date.year}, ${time.hour}:${time.minute}"
    } else if (date != null) {
        "${date.dayOfMonth}. ${date.monthNumber}. ${date.year}"
    } else if (time != null) {
        "${time.hour}:${time.minute}"
    } else {
        "DD. MM. YYYY, HH:MM"
    }
}