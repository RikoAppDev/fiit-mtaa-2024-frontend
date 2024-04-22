package auth.presentation.splash

data class SplashState(
    var isLoading: Boolean,
    val tokenValid: Boolean,
    val error: String?,
)
