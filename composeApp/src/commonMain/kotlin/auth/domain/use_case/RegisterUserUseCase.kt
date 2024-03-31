package auth.domain.use_case

import auth.data.remote.dto.AuthUserDto
import auth.domain.model.NewUser
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class RegisterUserUseCase(private val networkHandler: NetworkHandler) {
    operator fun invoke(newUser: NewUser): Flow<ResultHandler<AuthUserDto, DataError.NetworkError>> =
        networkHandler.invokeApi { registerUser(newUser = newUser) }
}