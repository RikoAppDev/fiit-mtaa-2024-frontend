package event.presentation.event_detail

import event.data.dto.EventDetailDto
import event.data.dto.EventWorkerDto
import event.data.dto.EventWorkersDto
import event.domain.EventDetailPermissions

data class EventDetailState(
    val isLoadingEventData: Boolean,
    val isLoadingWorkersData: Boolean,
    val isLoadingButton: Boolean,
    val isLoadingRefresh: Boolean,
    val eventDetail: EventDetailDto?,
    val eventWorkers: EventWorkersDto?,
    val error: String?,
    val userPermissions: EventDetailPermissions?,
    val isWorkerDetailExpanded: Boolean,
    val workerDetail: EventWorkerDto?

)
