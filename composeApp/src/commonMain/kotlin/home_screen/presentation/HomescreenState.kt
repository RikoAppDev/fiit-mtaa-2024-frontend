package home_screen.presentation

import core.data.remote.dto.EventCardListDto
import home_screen.data.ActiveEventDto

data class HomescreenState (
    val isLatestEventsLoading: Boolean,
    val isNearestEventsLoading: Boolean,
    val isActiveEventLoading: Boolean,
    val latestEvents: EventCardListDto?,
    val nearestEvents: EventCardListDto?,
    val activeEvent: ActiveEventDto?,
    val error: String
)