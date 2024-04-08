package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.ImageUploadDto
import kotlinx.coroutines.flow.Flow

class UploadImageUseCase(
    private val networkHandler: NetworkHandler,
    private val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(imageData: ByteArray): Flow<ResultHandler<ImageUploadDto, DataError.NetworkError>> =
        networkHandler.invokeApi {
            uploadImage(
                databaseClient.selectUserToken(),
                imageData = imageData
            )
        }
}