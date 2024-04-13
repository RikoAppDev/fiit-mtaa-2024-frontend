package event.presentation.create_update.component

import core.data.remote.dto.EventCategoryDto
import core.domain.event.SallaryType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface EventCreateUpdateScreenEvent {
    data object OnBackButtonClick : EventCreateUpdateScreenEvent
    data class UpdateImage(val image: ByteArray) : EventCreateUpdateScreenEvent
    data class UpdateTitle(val title: String) : EventCreateUpdateScreenEvent
    data class UpdateDescription(val description: String) : EventCreateUpdateScreenEvent
    data class UpdateCapacity(val capacity: String) : EventCreateUpdateScreenEvent
    data class UpdateDate(val date: LocalDate) : EventCreateUpdateScreenEvent
    data class UpdateTime(val time: LocalTime) : EventCreateUpdateScreenEvent
    data class UpdateRequiredTools(val requiredTools: String) : EventCreateUpdateScreenEvent
    data class UpdateProvidedTools(val providedTools: String) : EventCreateUpdateScreenEvent
    data class UpdateSearchLocation(val location: String) : EventCreateUpdateScreenEvent
    data class UpdateSalaryType(val salaryType: SallaryType) : EventCreateUpdateScreenEvent
    data class UpdateSalaryAmount(val salaryAmount: String) : EventCreateUpdateScreenEvent
    data class UpdateSalaryUnit(val salaryUnit: String) : EventCreateUpdateScreenEvent
    data class UpdateSalaryGoodTitle(val salaryGoodTitle: String) : EventCreateUpdateScreenEvent
    data class UpdateSearchCategory(val searchCategory: String) : EventCreateUpdateScreenEvent
    data class AddCategory(val category: EventCategoryDto) : EventCreateUpdateScreenEvent
    data class RemoveCategory(val index: Int) : EventCreateUpdateScreenEvent
    data object OnCreateEventButtonClick : EventCreateUpdateScreenEvent
    data object OnUpdateEventButtonClick : EventCreateUpdateScreenEvent
    data object RemoveError : EventCreateUpdateScreenEvent
}