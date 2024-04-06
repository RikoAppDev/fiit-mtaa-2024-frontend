package all_events_screen.domain.use_case

import core.data.database.SqlDelightDatabaseClient
import core.data.remote.dto.EventCardListDto
import core.domain.DataError
import core.domain.NetworkHandler
import core.domain.ResultHandler
import core.domain.event.SallaryType
import kotlinx.coroutines.flow.Flow

class LoadFilteredEventsUseCase(
    private val networkHandler: NetworkHandler,
    val databaseClient: SqlDelightDatabaseClient
) {
    operator fun invoke(
        filterCategory: String?,
        filterSallary: SallaryType?,
        filterDistance: Number?
    ): Flow<ResultHandler<EventCardListDto, DataError.NetworkError>> =
        networkHandler.invokeApi {
            getEventsFiltered(
                databaseClient.selectUserToken(),
                filterCategory,
                filterSallary,
                filterDistance
            )
        }
}