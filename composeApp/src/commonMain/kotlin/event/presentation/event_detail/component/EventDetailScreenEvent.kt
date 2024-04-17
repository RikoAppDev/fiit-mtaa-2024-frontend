package event.presentation.event_detail.component

import event.domain.model.EventNavigationStatus

sealed interface EventDetailScreenEvent {
    data object SignInForEvent : EventDetailScreenEvent
    data object SignOffEvent : EventDetailScreenEvent
    data object EditEvent : EventDetailScreenEvent
    data object StartEvent : EventDetailScreenEvent
    data object DeleteEvent : EventDetailScreenEvent
    data object NavigateBack : EventDetailScreenEvent
    data object Refresh : EventDetailScreenEvent
    data object OnLiveEventTagClick : EventDetailScreenEvent
    data object RemoveError : EventDetailScreenEvent
    data class ChangeNavigationStatus(val navStatus: EventNavigationStatus) :
        EventDetailScreenEvent
}