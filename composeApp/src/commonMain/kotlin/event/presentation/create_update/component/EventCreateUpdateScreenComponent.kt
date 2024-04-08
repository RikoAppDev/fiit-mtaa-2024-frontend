package event.presentation.create_update.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.domain.model.Event
import event.domain.use_case.UploadImageUseCase
import event.presentation.create_update.EventCreateUpdateState
import kotlinx.coroutines.launch

class EventCreateUpdateScreenComponent(
    componentContext: ComponentContext,
    private val uploadImageUseCase: UploadImageUseCase
) :
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
            is EventCreateUpdateScreenEvent.UploadImage -> {
                println(event.image)
                uploadImage(event.image)
            }
        }

    }

    fun uploadImage(image:ByteArray) {
        this@EventCreateUpdateScreenComponent.coroutineScope().launch {
            uploadImageUseCase(image).collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        println(result.data)
                    }

                    is ResultHandler.Error -> {
                        result.error.asUiText().asNonCompString()
                    }

                    is ResultHandler.Loading -> {
                        println("loading")
                    }
                }
            }
        }
    }
}