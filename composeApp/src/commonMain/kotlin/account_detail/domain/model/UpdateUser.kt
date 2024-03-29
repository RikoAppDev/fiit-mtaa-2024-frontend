package account_detail.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUser(
    val name:String,
    val phoneNumber:String,
)

