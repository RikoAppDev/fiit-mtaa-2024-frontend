package event.presentation.event_detail_live.component

import event.domain.model.PresenceStatus

sealed interface InProgressEventDetailScreenEvent {
    data object OnNavigateBack : InProgressEventDetailScreenEvent
    data object EndEvent : InProgressEventDetailScreenEvent
    data object SaveAttendance : InProgressEventDetailScreenEvent

    data object DiscardChanges : InProgressEventDetailScreenEvent
    data class AnnouncementInputChange(val text: String) : InProgressEventDetailScreenEvent
    data object AnnouncementPublish : InProgressEventDetailScreenEvent
    data class ModifyAttendance(val id: String, val status: PresenceStatus) :
        InProgressEventDetailScreenEvent

}