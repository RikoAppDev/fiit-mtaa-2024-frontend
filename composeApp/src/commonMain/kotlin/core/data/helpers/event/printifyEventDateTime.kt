package core.data.helpers.event

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun printifyEventDateTime(date:String):String{
    val date = Instant.parse(date).toLocalDateTime(TimeZone.UTC)

    return "${date.dayOfMonth}. ${date.monthNumber}. ${date.year}, ${date.hour}:${date.minute}"
}