package auth.domain

import core.domain.Error

sealed interface AuthError : Error {
    enum class EmailError : AuthError {
        INVALID_FORMAT
    }

    enum class PasswordError : Error {
        TOO_SHORT, NO_DIGIT, NO_MATCH
    }

    enum class TokenError : Error {
        EXPIRED
    }
}