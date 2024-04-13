package event.presentation.event_detail_live.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.domain.getInProgressEventDisplayConditions
import event.domain.use_case.LoadAttendanceDataUseCase
import event.domain.use_case.LoadInProgressEventDataUseCase
import event.presentation.event_detail.EventDetailState
import event.presentation.event_detail_live.InProgressEventDetailState
import kotlinx.coroutines.launch

class InProgressEventDetailScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val loadInProgressEventDataUseCase: LoadInProgressEventDataUseCase,
    private val loadAttendanceDataUseCase: LoadAttendanceDataUseCase,
    private val id: String,
    private val databaseClient: SqlDelightDatabaseClient
) : ComponentContext by componentContext {

    private val _inProgressEventDetailState = MutableValue(
        InProgressEventDetailState(
            isLoadingLiveEventData = true,
            isLoadingAttendanceData = false,
            attendanceData = null,
            liveEventData = null,
            errorAttendanceData = "",
            errorLiveEventData = "",
            permissions = null
        )
    )
    val inProgressEventDetailState: Value<InProgressEventDetailState> = _inProgressEventDetailState

    fun onEvent(event: InProgressEventDetailScreenEvent) {
        when (event) {
            InProgressEventDetailScreenEvent.OnNavigateBack -> {
                onNavigateBack()
            }
        }
    }

    fun loadInProgressEventData() {
        this@InProgressEventDetailScreenComponent.coroutineScope().launch {
            loadInProgressEventDataUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        val permissions =
                            getInProgressEventDisplayConditions(user = databaseClient.selectUser())
                        println(permissions)
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isLoadingLiveEventData = false,
                            liveEventData = result.data,
                            permissions = permissions
                        )
                        if(permissions.displayWorkers){
                            loadAttendanceData()
                        }
                    }

                    is ResultHandler.Error -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            errorLiveEventData = result.error.asUiText().asNonCompString(),
                        )
                    }

                    is ResultHandler.Loading -> {
                        _inProgressEventDetailState.value =
                            _inProgressEventDetailState.value.copy(isLoadingLiveEventData = true)
                    }
                }
            }
        }
    }

    private fun loadAttendanceData() {
        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
            isLoadingAttendanceData = true
        )
        this@InProgressEventDetailScreenComponent.coroutineScope().launch {
            loadAttendanceDataUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isLoadingAttendanceData = false,
                            attendanceData = result.data
                        )
                    }

                    is ResultHandler.Error -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            errorAttendanceData = result.error.asUiText().asNonCompString(),
                        )
                    }

                    is ResultHandler.Loading -> {
                        _inProgressEventDetailState.value =
                            _inProgressEventDetailState.value.copy(isLoadingAttendanceData = true)
                    }
                }
            }
        }
    }
}