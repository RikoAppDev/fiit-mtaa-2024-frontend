package auth.domain

import core.domain.ResultHandler

class AuthValidation {
    fun validateEmail(email: String): ResultHandler<Unit, AuthError.EmailError> {
        TODO()
    }

    fun validatePassword(password: String): ResultHandler<Unit, AuthError.PasswordError> {
        if (password.length < 8) {
            return ResultHandler.Error(AuthError.PasswordError.TOO_SHORT)
        }

        val hasDigit = password.all { it.isDigit() }
        if (!hasDigit) {
            return ResultHandler.Error(AuthError.PasswordError.NO_DIGIT)
        }

        return ResultHandler.Success(Unit)
    }

    fun matchPasswords(
        password: String,
        passwordRepeated: String
    ): ResultHandler<Unit, AuthError.PasswordError> {
        val hasMatch = password == passwordRepeated
        if (!hasMatch) {
            return ResultHandler.Error(AuthError.PasswordError.NO_MATCH)
        }

        return ResultHandler.Success(Unit)
    }
}