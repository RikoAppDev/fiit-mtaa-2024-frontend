package event.presentation.my

import all_events_screen.data.CategoriesWithCountDto
import core.data.remote.dto.EventCardListDto

data class MyEventsState(
    val isLoadingEvents: Boolean,
    val errorEvents: String?,
    val events: EventCardListDto?,
)