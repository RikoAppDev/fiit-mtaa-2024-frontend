package event.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PublishAnnouncementDto(
    val message: String,
)