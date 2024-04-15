package core.data.helpers

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun printifyTime(date: LocalDateTime): String {
    return "${date.hour.toString().padStart(2, '0')}:${date.minute.toString().padStart(2, '0')}"
}