package event.presentation.event_detail_live.component

sealed interface InProgressEventDetailScreenEvent {
    data object OnNavigateBack : InProgressEventDetailScreenEvent
}