package event_detail.presentation.event_detail_worker.component



sealed interface EventDetailScreenEvent {
    data object SignInForEvent : EventDetailScreenEvent

}