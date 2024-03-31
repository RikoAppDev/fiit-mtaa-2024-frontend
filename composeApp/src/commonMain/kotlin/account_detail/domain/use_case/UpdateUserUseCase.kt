package account_detail.domain.use_case

import account_detail.domain.model.UpdateUser
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class UpdateUserUseCase(private val networkHandler: NetworkHandler) {
    operator fun invoke(
        updateUser: UpdateUser,
        token: String
    ): Flow<ResultHandler<String, DataError.NetworkError>> =
        networkHandler.invokeApi { updateUser(updateUser, token) }
}