package auth.domain

import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthValidation {
    suspend fun validateEmail(email: String): Flow<ResultHandler<Boolean, AuthError.EmailError>> =
        flow {
            emit(ResultHandler.Loading())
            val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$")
            if (emailRegex.matches(email)) {
                emit(ResultHandler.Success(true))
            } else {
                emit(ResultHandler.Error(AuthError.EmailError.INVALID_FORMAT))
            }
        }

    suspend fun validatePassword(password: String): Flow<ResultHandler<Boolean, AuthError.PasswordError>> =
        flow {
            if (password.length < 8) {
                emit(ResultHandler.Error(AuthError.PasswordError.TOO_SHORT))
            } else {
                println(password)
                val hasDigit = password.any { it.isDigit() }
                if (!hasDigit) {
                    emit(ResultHandler.Error(AuthError.PasswordError.NO_DIGIT))
                } else {
                    emit(ResultHandler.Success(true))
                }
            }
        }

    suspend fun matchPasswords(
        password: String,
        passwordRepeated: String
    ): Flow<ResultHandler<Boolean, AuthError.PasswordError>> = flow {
        if (password != passwordRepeated) {
            emit(ResultHandler.Error(AuthError.PasswordError.NO_MATCH))
        } else {
            emit(ResultHandler.Success(true))
        }
    }

    suspend fun validateName(
        name: String,
    ): Flow<ResultHandler<Boolean, AuthError.ProfileError>> = flow {
        if (name.isEmpty()) {
            emit(ResultHandler.Error(AuthError.ProfileError.EMPTY_NAME))
        } else {
            emit(ResultHandler.Success(true))
        }
    }
}