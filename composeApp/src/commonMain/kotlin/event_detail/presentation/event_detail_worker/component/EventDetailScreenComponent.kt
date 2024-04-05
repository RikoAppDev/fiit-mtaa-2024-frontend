package event_detail.presentation.event_detail_worker.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event_detail.domain.getUserPermissions
import event_detail.domain.use_case.LoadEventDataUseCase
import event_detail.domain.use_case.LoadEventWorkers
import event_detail.presentation.event_detail_worker.EventDetailState
import kotlinx.coroutines.launch

class EventDetailScreenComponent(
    componentContext: ComponentContext,
    private val loadEventDataUseCase: LoadEventDataUseCase,
    private val loadEventWorkersUseCase: LoadEventWorkers,
    private val id: String,
    private val onNavigateBack: () -> Unit,
    val databaseClient: SqlDelightDatabaseClient
) : ComponentContext by componentContext {

    private val _stateEventDetail = MutableValue(
        EventDetailState(
            isLoadingEventData = false,
            isLoadingWorkersData = false,
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
            is EventDetailScreenEvent.SignInForEvent -> handleSignInForEvent() // TODO
            EventDetailScreenEvent.NavigateBack -> {
                onNavigateBack()
            }

            EventDetailScreenEvent.EditEvent -> TODO()
            EventDetailScreenEvent.SignOffEvent -> TODO()
            EventDetailScreenEvent.StartEvent -> TODO()
            is EventDetailScreenEvent.WorkerDetail -> TODO()
        }
    }

    private fun handleSignInForEvent() {
        print("Wocap")
    }

    private fun loadWorkersData(){
        this@EventDetailScreenComponent.coroutineScope().launch {
            loadEventWorkersUseCase(id).collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        println("WORKERS:" + result.data)
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
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        val permissions = getUserPermissions(
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