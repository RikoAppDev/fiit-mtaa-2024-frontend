package event.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import event.data.dto.AnnouncementItemDto
import event.data.dto.AnnouncementItemWS
import kotlinx.coroutines.flow.Flow

class SubscribeToAnnouncementsUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient,
) {
    operator fun invoke(
        id: String,
        onNewAnnouncement: (announcement: AnnouncementItemWS) -> Unit,
    ): Flow<ResultHandler<String, DataError.NetworkError>> =
        networkHandler.invokeApi {
            subscribeToAnnouncements(
                databaseClient.selectUserToken(),
                id,
                onNewAnnouncement
            )
        }
}