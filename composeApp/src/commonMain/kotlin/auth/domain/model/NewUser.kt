package auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
class NewUser(
    val email:String = "",
    val password:String = "",
    val accountType: AccountType = AccountType.HARVESTER,
    val name: String = "",
    val phoneNumber: String = "",
)
