package event.presentation.event_detail.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.data.dto.toEventState
import event.domain.getEventDetailDisplayConditions
import event.domain.use_case.LoadEventDataUseCase
import event.domain.use_case.LoadEventWorkersUseCase
import event.domain.use_case.SignInForEventUseCase
import event.domain.use_case.SignOffEventUseCase
import event.domain.use_case.StartEventUseCase
import event.presentation.create_update.EventState
import event.presentation.event_detail.EventDetailState
import kotlinx.coroutines.launch

class EventDetailScreenComponent(
    componentContext: ComponentContext,
    private val loadEventDataUseCase: LoadEventDataUseCase,
    private val loadEventWorkersUseCase: LoadEventWorkersUseCase,
    private val signInForEventUseCase: SignInForEventUseCase,
    private val signOffEventUseCase: SignOffEventUseCase,
    private val startEventUseCase: StartEventUseCase,
    private val id: String,
    private val onNavigateBack: () -> Unit,
    private val navigateToEditEvent: (event: EventState) -> Unit,
    private val databaseClient: SqlDelightDatabaseClient,
    private val onNavigateToLiveEvent: (id: String) -> Unit,
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
            isLoadingRefresh = false
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

            is EventDetailScreenEvent.EditEvent -> {
                if (_stateEventDetail.value.eventDetail != null) {
                    navigateToEditEvent(
                        _stateEventDetail.value.eventDetail!!.toEventState(
                            _stateEventDetail.value.eventDetail!!
                        )
                    )
                }
            }

            is EventDetailScreenEvent.StartEvent -> {
                startEvent(id)
            }

            is EventDetailScreenEvent.Refresh -> {
                _stateEventDetail.value = _stateEventDetail.value.copy(
                    isLoadingRefresh = true
                )
                loadEventData(true)
            }

            is EventDetailScreenEvent.OnLiveEventTagClick -> {
                onNavigateToLiveEvent(id)
            }
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
                        loadEventData(true)
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value =
                            _stateEventDetail.value.copy(isLoadingButton = true)
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
                        loadEventData(true)
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value =
                            _stateEventDetail.value.copy(isLoadingButton = true)
                    }
                }
            }
        }
    }

    private fun loadWorkersData() {
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
                        _stateEventDetail.value =
                            _stateEventDetail.value.copy(isLoadingWorkersData = true)
                    }
                }
            }
        }
    }

    private fun startEvent(id: String) {
        _stateEventDetail.value = _stateEventDetail.value.copy(
            isLoadingButton = true
        )

        this@EventDetailScreenComponent.coroutineScope().launch {
            startEventUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        onNavigateToLiveEvent(id)
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString(),
                            isLoadingButton = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value =
                            _stateEventDetail.value.copy(isLoadingButton = true)
                    }
                }
            }
        }
    }

    fun loadEventData(isRefresh: Boolean = false) {
        this@EventDetailScreenComponent.coroutineScope().launch {
            loadEventDataUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        val permissions = getEventDetailDisplayConditions(
                            result.data,
                            databaseClient.selectUser()
                        )
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            isLoadingEventData = false,
                            eventDetail = result.data,
                            userPermissions = permissions,
                            isLoadingRefresh = false
                        )
                        if (permissions.displayOrganiserControls) {
                            loadWorkersData()
                        }
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString(),
                            isLoadingRefresh = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        if (!isRefresh) {
                            _stateEventDetail.value =
                                _stateEventDetail.value.copy(isLoadingEventData = true)
                        }
                    }
                }
            }
        }
    }
}