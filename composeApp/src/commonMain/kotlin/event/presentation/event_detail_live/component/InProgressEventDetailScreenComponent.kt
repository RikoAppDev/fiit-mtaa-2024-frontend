package event.presentation.event_detail_live.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.data.dto.AttendanceDataDto
import event.data.dto.AttendanceUpdateDto
import event.data.dto.AttendanceUpdateListDto
import event.domain.getInProgressEventDisplayConditions
import event.domain.model.PresenceStatus
import event.domain.use_case.LoadAttendanceDataUseCase
import event.domain.use_case.LoadInProgressEventDataUseCase
import event.domain.use_case.UpdateAttendanceUseCase
import event.presentation.event_detail.EventDetailState
import event.presentation.event_detail_live.InProgressEventDetailState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class InProgressEventDetailScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val loadInProgressEventDataUseCase: LoadInProgressEventDataUseCase,
    private val loadAttendanceDataUseCase: LoadAttendanceDataUseCase,
    private val id: String,
    private val databaseClient: SqlDelightDatabaseClient,
    private val updateAttendanceUseCase: UpdateAttendanceUseCase
) : ComponentContext by componentContext {

    private val _inProgressEventDetailState = MutableValue(
        InProgressEventDetailState(
            isLoadingLiveEventData = true,
            isLoadingAttendanceData = false,
            isLoadingAttendanceUpdate = false,
            attendanceData = null,
            liveEventData = null,
            errorAttendanceData = "",
            errorLiveEventData = "",
            permissions = null,
            isAttendanceUpdated = false,
            updatedAttendanceData = null,
        )
    )
    val inProgressEventDetailState: Value<InProgressEventDetailState> = _inProgressEventDetailState

    private val _announcementText = MutableValue("")
    val announcementText: Value<String> = _announcementText

    fun onEvent(event: InProgressEventDetailScreenEvent) {
        when (event) {
            InProgressEventDetailScreenEvent.OnNavigateBack -> {
                onNavigateBack()
            }

            is InProgressEventDetailScreenEvent.ModifyAttendance -> {
                val updatedWorkers =
                    _inProgressEventDetailState.value.updatedAttendanceData!!.workers.map { worker ->
                        val modifiedWorker = worker.copy()
                        if (worker.user.id == event.id) {
                            modifiedWorker.presenceStatus = event.status

                            if (event.status == PresenceStatus.PRESENT) {
                                modifiedWorker.arrivedAt = Clock.System.now()
                            }

                            if (event.status == PresenceStatus.LEFT) {
                                modifiedWorker.leftAt = Clock.System.now()
                            }
                        }

                        modifiedWorker
                    }

                _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                    isAttendanceUpdated = true,
                    updatedAttendanceData = _inProgressEventDetailState.value.updatedAttendanceData!!.copy(
                        workers = updatedWorkers
                    )
                )
            }

            is InProgressEventDetailScreenEvent.AnnouncementInputChange -> {
                _announcementText.value = event.text
            }

            is InProgressEventDetailScreenEvent.SaveAttendance -> {
                if (_inProgressEventDetailState.value.isAttendanceUpdated) {
                    _inProgressEventDetailState.value.updatedAttendanceData?.let {
                        updateAttendance(
                            it
                        )
                    }
                }
            }

            is InProgressEventDetailScreenEvent.DiscardChanges -> {
                _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                    updatedAttendanceData = _inProgressEventDetailState.value.attendanceData,
                    isAttendanceUpdated = false
                )
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
                        if (permissions.displayWorkers) {
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

    private fun updateAttendance(attendance: AttendanceDataDto) {

        val dto = attendance.workers.map {
            AttendanceUpdateDto(
                userID = it.user.id,
                presenceStatus = it.presenceStatus,
                arrivedAt = if (it.arrivedAt != null) it.arrivedAt!!.toString() else null,
                leftAt = if (it.leftAt != null) it.leftAt!!.toString() else null,
            )
        }

        this@InProgressEventDetailScreenComponent.coroutineScope().launch {
            _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                isLoadingAttendanceUpdate = true
            )
            updateAttendanceUseCase(
                id,
                attendance = AttendanceUpdateListDto(workers = dto)
            ).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        attendance.lastUpdated = Clock.System.now()
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isAttendanceUpdated = false,
                            attendanceData = attendance,
                            updatedAttendanceData = attendance,
                            isLoadingAttendanceUpdate = false
                        )
                    }

                    is ResultHandler.Error -> {
                        println("ERROR: ${result.error}")
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            errorLiveEventData = result.error.asUiText().asNonCompString(),
                            isLoadingAttendanceUpdate = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isLoadingAttendanceUpdate = true
                        )
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
                            attendanceData = result.data,
                            updatedAttendanceData = result.data
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