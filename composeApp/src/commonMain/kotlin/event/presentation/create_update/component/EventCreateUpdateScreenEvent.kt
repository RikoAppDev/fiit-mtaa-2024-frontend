package event.presentation.create_update.component

import event.domain.model.Event
import event.domain.model.SalaryType
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
    data class UpdateLocation(val location: String) : EventCreateUpdateScreenEvent
    data class UpdateSalaryType(val salaryType: SalaryType) : EventCreateUpdateScreenEvent
    data class UpdateSalaryAmount(val salaryAmount: String) : EventCreateUpdateScreenEvent
    data class UpdateSalaryUnit(val salaryUnit: String) : EventCreateUpdateScreenEvent
    data class UpdateSalaryGoodTitle(val salaryGoodTitle: String) : EventCreateUpdateScreenEvent
    data class UpdateSearchCategory(val searchCategory: String) : EventCreateUpdateScreenEvent
    data class AddCategory(val category: String) : EventCreateUpdateScreenEvent
    data class OnCreateEventButtonClick(val event: Event) : EventCreateUpdateScreenEvent
    data class OnUpdateEventButtonClick(val event: Event) : EventCreateUpdateScreenEvent
}