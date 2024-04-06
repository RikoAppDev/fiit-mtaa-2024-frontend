package event.domain.model

import kotlinx.datetime.LocalDateTime

data class Event(
    val title: String?,
    val description: String?,
    val capacity: Int?,
    val datetime: LocalDateTime?,
    val requiredTools: String?,
    val providedTools: String?,
    val location: String?
)
