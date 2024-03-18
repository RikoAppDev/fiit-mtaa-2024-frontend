package event_detail.presentation.event_create.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class EventCreateScreenComponent(componentContext: ComponentContext):ComponentContext by componentContext {
    private val _eventName = MutableValue("")
    val eventName: Value<String> = _eventName

    private val _description = MutableValue("")
    val description: Value<String> = _description
    fun onEvent(event: EventCreateScreenEvent){
        when (event){
            is EventCreateScreenEvent.ChangeEventName -> {
                _eventName.value = event.eventName
            }

            is EventCreateScreenEvent.ChangeEventDescription -> {
                _description.value = event.description
            }
        }

    }
}