package event.presentation.event_detail.component


sealed interface EventDetailScreenEvent {
    data object SignInForEvent : EventDetailScreenEvent
    data object SignOffEvent : EventDetailScreenEvent
    data object EditEvent : EventDetailScreenEvent
    data object StartEvent : EventDetailScreenEvent
    data object NavigateBack: EventDetailScreenEvent
    data object Refresh: EventDetailScreenEvent
    data object OnLiveEventTagClick: EventDetailScreenEvent
}