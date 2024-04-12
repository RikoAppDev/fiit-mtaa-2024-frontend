package event.presentation.create_update.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.domain.model.Event
import event.domain.model.SalaryType
import event.domain.use_case.CreateEventUseCase
import event.domain.use_case.UpdateEventUseCase
import event.domain.use_case.UploadImageUseCase
import event.presentation.create_update.EventCreateUpdateState
import event.presentation.create_update.EventState
import event.presentation.create_update.toEvent
import kotlinx.coroutines.launch

class EventCreateUpdateScreenComponent(
    componentContext: ComponentContext,
    private val uploadImageUseCase: UploadImageUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    private val _stateEventCreateUpdate =
        MutableValue(
            EventCreateUpdateState(
                isLoading = false,
                event = null,
                error = null
            )
        )
    val stateEventCreateUpdate: Value<EventCreateUpdateState> = _stateEventCreateUpdate

    private val _stateEvent = MutableValue(
        EventState(
            image = null,
            title = "",
            description = "",
            capacity = "",
            date = null,
            time = null,
            requiredTools = "",
            providedTools = "",
            searchLocation = "",
            salaryType = SalaryType.MONEY,
            salaryAmount = "",
            salaryUnit = "",
            salaryGoodTitle = "",
            searchCategory = "",
            categoryList = mutableListOf()
        ),
    )
    val stateEvent: Value<EventState> = _stateEvent

    fun onEvent(event: EventCreateUpdateScreenEvent) {
        when (event) {
            is EventCreateUpdateScreenEvent.OnBackButtonClick -> {
                onNavigateBack()
            }

            is EventCreateUpdateScreenEvent.UpdateImage -> {
                _stateEvent.value = _stateEvent.value.copy(
                    image = event.image
                )
            }

            is EventCreateUpdateScreenEvent.UpdateTitle -> {
                _stateEvent.value = _stateEvent.value.copy(
                    title = event.title
                )
            }

            is EventCreateUpdateScreenEvent.UpdateDescription -> {
                _stateEvent.value = _stateEvent.value.copy(
                    description = event.description
                )
            }

            is EventCreateUpdateScreenEvent.UpdateCapacity -> {
                _stateEvent.value = _stateEvent.value.copy(
                    capacity = event.capacity
                )
            }

            is EventCreateUpdateScreenEvent.UpdateDate -> {
                _stateEvent.value = _stateEvent.value.copy(
                    date = event.date
                )
            }

            is EventCreateUpdateScreenEvent.UpdateTime -> {
                _stateEvent.value = _stateEvent.value.copy(
                    time = event.time
                )
            }

            is EventCreateUpdateScreenEvent.UpdateLocation -> {
                _stateEvent.value = _stateEvent.value.copy(
                    searchLocation = event.location
                )
            }

            is EventCreateUpdateScreenEvent.UpdateProvidedTools -> {
                _stateEvent.value = _stateEvent.value.copy(
                    providedTools = event.providedTools
                )
            }

            is EventCreateUpdateScreenEvent.UpdateRequiredTools -> {
                _stateEvent.value = _stateEvent.value.copy(
                    requiredTools = event.requiredTools
                )
            }

            is EventCreateUpdateScreenEvent.UpdateSearchCategory -> {
                _stateEvent.value = _stateEvent.value.copy(
                    searchCategory = event.searchCategory,
                )
            }

            is EventCreateUpdateScreenEvent.AddCategory -> {
                _stateEvent.value.categoryList.add(event.category)
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryAmount -> {
                _stateEvent.value = _stateEvent.value.copy(
                    salaryAmount = event.salaryAmount
                )
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryGoodTitle -> {
                _stateEvent.value = _stateEvent.value.copy(
                    salaryGoodTitle = event.salaryGoodTitle
                )
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryType -> {
                _stateEvent.value = _stateEvent.value.copy(
                    salaryType = event.salaryType
                )
            }

            is EventCreateUpdateScreenEvent.UpdateSalaryUnit -> {
                _stateEvent.value = _stateEvent.value.copy(
                    salaryUnit = event.salaryUnit
                )
            }

            is EventCreateUpdateScreenEvent.OnCreateEventButtonClick -> {
                _stateEventCreateUpdate.value = _stateEventCreateUpdate.value.copy(
                    event = _stateEvent.value.toEvent(_stateEvent.value)
                )
                if (_stateEventCreateUpdate.value.event?.image != null) {
                    uploadImage(_stateEventCreateUpdate.value.event!!.image!!)
                }
                if (_stateEventCreateUpdate.value.event != null) {
                    createEvent(_stateEventCreateUpdate.value.event!!)
                }
            }

            is EventCreateUpdateScreenEvent.OnUpdateEventButtonClick -> {
                if (_stateEventCreateUpdate.value.event?.image != null) {
                    uploadImage(_stateEventCreateUpdate.value.event!!.image!!)
                }
                if (_stateEventCreateUpdate.value.event != null) {
                    updateEvent(_stateEventCreateUpdate.value.event!!)
                }
            }
        }
    }

    private fun uploadImage(image: ByteArray) {
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

    private fun createEvent(event: Event) {
        this@EventCreateUpdateScreenComponent.coroutineScope().launch {
            /*createEventUseCase(event).collect { result ->
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
            }*/
        }
    }

    private fun updateEvent(event: Event) {
        this@EventCreateUpdateScreenComponent.coroutineScope().launch {
            /*updateEventUseCase(event).collect { result ->
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
            }*/
        }
    }
}