package event.presentation.detail_worker.component


sealed interface EventDetailScreenEvent {
    data object SignInForEvent : EventDetailScreenEvent
    data object SignOffEvent : EventDetailScreenEvent
    data object EditEvent : EventDetailScreenEvent
    data object StartEvent : EventDetailScreenEvent

    data object NavigateBack: EventDetailScreenEvent

}