package auth.domain.use_case

import auth.data.remote.dto.AuthUserDto
import auth.domain.model.Login
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class LoginUserUseCase(private val networkHandler: NetworkHandler) {
    operator fun invoke(login: Login): Flow<ResultHandler<AuthUserDto, DataError.NetworkError>> =
        networkHandler.invokeApi { loginUser(login) }
}