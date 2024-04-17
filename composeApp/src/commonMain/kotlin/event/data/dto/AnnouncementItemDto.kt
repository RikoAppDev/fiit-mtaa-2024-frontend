package event.data.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
class AnnouncementItemWS(
    val message: String,
    val createdAt: Instant,
)