package auth.presentation.login

data class LoginState(
    var isLoading: Boolean,
    val email: String,
    val password: String,
    val dataError: String?,
    val emailError: String?,
    val passwordError: String?,
)
