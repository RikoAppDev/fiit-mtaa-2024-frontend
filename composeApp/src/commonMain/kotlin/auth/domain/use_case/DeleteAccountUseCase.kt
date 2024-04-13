package auth.domain.use_case

import auth.data.remote.dto.AuthUserDto
import auth.domain.model.Login
import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import kotlinx.coroutines.flow.Flow

class DeleteAccountUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(): Flow<ResultHandler<String, DataError.NetworkError>> =
        networkHandler.invokeApi { deleteUserAccount(databaseClient.selectUserToken()) }
}