package event.data.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.*

@Serializable
data class LiveEventDataDto(
    val announcementItems: List<AnnouncementItemDto>,
    val harmonogramItems: List<HarmonogramItemDto>,
    val name:String
)

@Serializable
data class AnnouncementItemDto(
    val createdAt: Instant,

    @SerialName("eventId")
    val eventID: String,

    val id: String,
    val message: String,

    @SerialName("userId")
    val userID: String
)

@Serializable
data class HarmonogramItemDto(
    val createdAt: Instant,
    val description: String?,

    @SerialName("eventId")
    val eventID: String,

    val from: String,
    val id: String,
    val title: String,
    val to: String
)