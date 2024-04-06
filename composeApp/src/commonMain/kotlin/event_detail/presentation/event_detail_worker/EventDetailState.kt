package event_detail.presentation.event_detail_worker

import event_detail.data.dto.EventDetailDto
import event_detail.data.dto.EventWorkerDto
import event_detail.data.dto.EventWorkersDto
import event_detail.domain.UserPermissions

data class EventDetailState(
    val isLoadingEventData: Boolean,
    val isLoadingWorkersData: Boolean,
    val isLoadingButton: Boolean,
    val eventDetail: EventDetailDto?,
    val eventWorkers: EventWorkersDto?,
    val error: String?,
    val userPermissions: UserPermissions?,
    val isWorkerDetailExpanded: Boolean,
    val workerDetail: EventWorkerDto?

)
