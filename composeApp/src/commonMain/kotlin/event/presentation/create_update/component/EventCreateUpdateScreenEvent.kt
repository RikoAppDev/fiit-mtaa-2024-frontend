package event.presentation.create_update.component

import kotlinx.datetime.LocalDateTime

sealed interface EventCreateUpdateScreenEvent {
    data class UpdateTitle(val title: String) : EventCreateUpdateScreenEvent
    data class UpdateDescription(val description: String) : EventCreateUpdateScreenEvent
    data class UpdateCapacity(val capacity: Int) : EventCreateUpdateScreenEvent
    data class UpdateDate(val date: LocalDateTime) : EventCreateUpdateScreenEvent
    data class UpdateRequiredTools(val requiredTools: String) : EventCreateUpdateScreenEvent
    data class UpdateProvidedTools(val providedTools: String) : EventCreateUpdateScreenEvent
    data class UpdateLocation(val location: String) : EventCreateUpdateScreenEvent
}