package event.presentation.event_detail.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.domain.getDisplayConditions
import event.domain.use_case.LoadEventDataUseCase
import event.domain.use_case.LoadEventWorkersUseCase
import event.domain.use_case.SignInForEventUseCase
import event.domain.use_case.SignOffEventUseCase
import event.presentation.event_detail.EventDetailState
import kotlinx.coroutines.launch

class EventDetailScreenComponent(
    componentContext: ComponentContext,
    private val loadEventDataUseCase: LoadEventDataUseCase,
    private val loadEventWorkersUseCase: LoadEventWorkersUseCase,
    private val signInForEventUseCase: SignInForEventUseCase,
    private val signOffEventUseCase: SignOffEventUseCase,
    private val id: String,
    private val onNavigateBack: () -> Unit,
    val databaseClient: SqlDelightDatabaseClient
) : ComponentContext by componentContext {

    private val _stateEventDetail = MutableValue(
        EventDetailState(
            isLoadingEventData = true,
            isLoadingWorkersData = false,
            isLoadingButton = false,
            eventDetail = null,
            eventWorkers = null,
            error = null,
            userPermissions = null,
            isWorkerDetailExpanded = false,
            workerDetail = null,

        )
    )
    val stateEventDetail: Value<EventDetailState> = _stateEventDetail

    fun onEvent(event: EventDetailScreenEvent) {
        when (event) {
            is EventDetailScreenEvent.SignInForEvent -> {
                signInForEvent()
            }

            is EventDetailScreenEvent.NavigateBack -> {
                onNavigateBack()
            }

            is EventDetailScreenEvent.SignOffEvent -> {
                signOffEvent()
            }

            is EventDetailScreenEvent.EditEvent -> TODO()
            is EventDetailScreenEvent.StartEvent -> TODO()
        }
    }

    private fun signInForEvent() {
        this@EventDetailScreenComponent.coroutineScope().launch {
            _stateEventDetail.value = _stateEventDetail.value.copy(
                isLoadingButton = true
            )
            signInForEventUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            isLoadingButton = false,
                            isLoadingEventData = true,
                        )
                        // Reload page
                        loadEventData()
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(isLoadingButton = true)
                    }
                }
            }
        }
    }

    private fun signOffEvent() {
        this@EventDetailScreenComponent.coroutineScope().launch {
            _stateEventDetail.value = _stateEventDetail.value.copy(
                isLoadingButton = true
            )
            signOffEventUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            isLoadingButton = false,
                            isLoadingEventData = true,
                        )
                        // Reload page
                        loadEventData()
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(isLoadingButton = true)
                    }
                }
            }
        }
    }

    private fun loadWorkersData(){
        this@EventDetailScreenComponent.coroutineScope().launch {
            loadEventWorkersUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            isLoadingWorkersData = false,
                            eventWorkers = result.data
                        )
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(isLoadingWorkersData = true)
                    }
                }
            }
        }
    }

    fun loadEventData() {
        this@EventDetailScreenComponent.coroutineScope().launch {
            loadEventDataUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        val permissions = getDisplayConditions(
                            result.data,
                            databaseClient.selectUser()
                        )
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            isLoadingEventData = false,
                            eventDetail = result.data,
                            userPermissions = permissions
                        )
                        if(permissions.displayOrganiserControls){
                            loadWorkersData()
                        }
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(isLoadingEventData = true)
                    }
                }
            }
        }
    }
}