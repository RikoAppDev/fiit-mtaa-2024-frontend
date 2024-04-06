package event_detail.presentation.event_detail_worker.component

import event_detail.data.dto.EventWorkerDto


sealed interface EventDetailScreenEvent {
    data object SignInForEvent : EventDetailScreenEvent
    data object SignOffEvent : EventDetailScreenEvent
    data object EditEvent : EventDetailScreenEvent
    data object StartEvent : EventDetailScreenEvent

    data object NavigateBack: EventDetailScreenEvent

}