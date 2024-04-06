package all_events_screen.presentation

import all_events_screen.data.CategoriesWithCountDto
import core.data.remote.dto.EventCardListDto

data class AllEventsState (
    val isLoadingCategories: Boolean,
    val isLoadingEvents: Boolean,
    val categories: CategoriesWithCountDto?,
    val events: EventCardListDto?
)