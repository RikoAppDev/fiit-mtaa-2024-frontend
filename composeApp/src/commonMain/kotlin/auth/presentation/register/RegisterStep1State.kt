package auth.presentation.register

data class RegisterStep1State(
    val email: String,
    val password: String,
    val passwordRepeated: String,
    val error: String?
)
