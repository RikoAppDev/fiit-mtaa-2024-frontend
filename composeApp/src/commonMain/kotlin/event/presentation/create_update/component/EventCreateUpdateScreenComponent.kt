package event.presentation.create_update.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import event.domain.model.Event
import event.presentation.create_update.EventCreateUpdateState

class EventCreateUpdateScreenComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext {
    private val _stateEventCreateUpdate =
        MutableValue(
            EventCreateUpdateState(
                isLoading = false,
                event = Event(
                    title = null,
                    description = null,
                    capacity = null,
                    datetime = null,
                    requiredTools = null,
                    providedTools = null,
                    location = null
                ),
                error = null
            )
        )
    val stateEventCreateUpdate: Value<EventCreateUpdateState> = _stateEventCreateUpdate

    fun onEvent(event: EventCreateUpdateScreenEvent) {
        when (event) {
            is EventCreateUpdateScreenEvent.UpdateTitle -> {
            }

            is EventCreateUpdateScreenEvent.UpdateDescription -> {
            }

            is EventCreateUpdateScreenEvent.UpdateCapacity -> TODO()
            is EventCreateUpdateScreenEvent.UpdateDate -> TODO()
            is EventCreateUpdateScreenEvent.UpdateLocation -> TODO()
            is EventCreateUpdateScreenEvent.UpdateProvidedTools -> TODO()
            is EventCreateUpdateScreenEvent.UpdateRequiredTools -> TODO()
        }

    }
}