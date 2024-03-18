package event_detail.presentation.event_create.component



sealed interface EventCreateScreenEvent {
    data class ChangeEventName(val eventName:String): EventCreateScreenEvent
    data class ChangeEventDescription(val description:String):EventCreateScreenEvent
}