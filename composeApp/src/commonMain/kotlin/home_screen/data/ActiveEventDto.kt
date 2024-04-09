package home_screen.data

// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json     = Json { allowStructuredMapKeys = true }
// val response = json.parse(Response.serializer(), jsonString)

import kotlinx.serialization.*


@Serializable
data class ActiveEventDto (
    val id: String,
    val name: String,
    val thumbnailURL: String? = null,

    @SerialName("User")
    val user: User
)

@Serializable
data class User (
    val name: String
)