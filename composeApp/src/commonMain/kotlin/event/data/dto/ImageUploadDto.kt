package event.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadDto (
    val imageURL:String
)