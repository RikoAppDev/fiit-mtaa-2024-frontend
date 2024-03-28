package auth.data.remote.dto

import auth.domain.model.AccountType
import com.grabit.User
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val token: String,
    val email: String,
    val accountType: AccountType,
    val name: String,
    val phoneNumber: String,
)

fun RegisterUserDto.toUser(): User {
    return User(
        token = token,
        email = email,
        accountType = if (accountType == AccountType.HARVESTER) {
            "HARVESTER"
        } else {
            "ORGANISER"
        },
        name = name,
        phoneNumber = phoneNumber
    )
}
