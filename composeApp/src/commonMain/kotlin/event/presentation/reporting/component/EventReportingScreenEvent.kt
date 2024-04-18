package event.presentation.reporting.component

sealed interface EventReportingScreenEvent {
    data object OnNavigateBack : EventReportingScreenEvent
}