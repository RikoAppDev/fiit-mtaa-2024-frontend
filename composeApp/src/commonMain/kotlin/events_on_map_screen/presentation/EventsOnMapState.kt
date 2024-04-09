package events_on_map_screen.presentation

import core.domain.EventMarker
import event.data.dto.EventDetailDto
import events_on_map_screen.data.PointListDto

data class EventsOnMapState (
    val isLoadingPoints:Boolean,
    val isLoadingEventSneakPeak:Boolean,
    val points:List<EventMarker>?,
    val eventSneakPeak:EventDetailDto?
)