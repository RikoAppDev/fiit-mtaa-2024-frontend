package auth.presentation.register

import auth.domain.model.NewUser

data class RegisterStep3State(
    val isLoading: Boolean,
    val newUser: NewUser,
    val name: String,
    val phoneNumber: String,
    val error: String?
)
