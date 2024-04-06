package event.presentation.detail_worker

import event.data.dto.EventDetailDto
import event.data.dto.EventWorkerDto
import event.data.dto.EventWorkersDto
import event.domain.UserPermissions

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
