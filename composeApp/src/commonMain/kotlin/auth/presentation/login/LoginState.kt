package auth.presentation.login

data class LoginState(
    val isLoading: Boolean,
    val email: String,
    val password: String,
    val dataError: String?,
    val emailError: String?,
    val passwordError: String?,
)
