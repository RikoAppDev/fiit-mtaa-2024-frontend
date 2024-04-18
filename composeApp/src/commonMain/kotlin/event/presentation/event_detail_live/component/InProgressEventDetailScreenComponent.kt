package event.presentation.event_detail_live.component

import auth.domain.model.AccountType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.grabit.SelectLastUpdated
import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.ResultHandler
import core.domain.worker.AssignmentStatus
import core.presentation.error_string_mapper.asUiText
import event.data.dto.AnnouncementItemDto
import event.data.dto.AnnouncementItemWS
import event.data.dto.AttendanceDataDto
import event.data.dto.AttendanceUpdateDto
import event.data.dto.AttendanceUpdateListDto
import event.data.dto.AttendanceUserDto
import event.data.dto.AttendanceWorkerDto
import event.domain.getInProgressEventDisplayConditions
import event.domain.model.PresenceStatus
import event.domain.model.convertToPresenceStatus
import event.domain.use_case.EndEventUseCase
import event.domain.use_case.LoadAttendanceDataUseCase
import event.domain.use_case.LoadInProgressEventDataUseCase
import event.domain.use_case.PublishAnnouncementUseCase
import event.domain.use_case.SubscribeToAnnouncementsUseCase
import event.domain.use_case.UpdateAttendanceUseCase
import event.presentation.event_detail_live.InProgressEventDetailState
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class InProgressEventDetailScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
    private val loadInProgressEventDataUseCase: LoadInProgressEventDataUseCase,
    private val loadAttendanceDataUseCase: LoadAttendanceDataUseCase,
    private val subscribeToAnnouncementsUseCase: SubscribeToAnnouncementsUseCase,
    private val publishAnnouncementUseCase: PublishAnnouncementUseCase,
    private val id: String,
    private val databaseClient: SqlDelightDatabaseClient,
    private val updateAttendanceUseCase: UpdateAttendanceUseCase,
    private val endEventUseCase: EndEventUseCase,

    ) : ComponentContext by componentContext {

    private val _inProgressEventDetailState = MutableValue(
        InProgressEventDetailState(
            isOffline = false,
            isLoadingLiveEventData = true,
            isLoadingAttendanceData = false,
            isLoadingAttendanceUpdate = false,
            isLoadingPublish = false,
            isLoadingEventEnd = false,
            attendanceData = null,
            liveEventData = null,
            errorAttendanceData = "",
            errorLiveEventData = "",
            permissions = getInProgressEventDisplayConditions(databaseClient.selectUser()),
            isAttendanceUpdated = false,
            updatedAttendanceData = null,
            isNotPresent = false,
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

            InProgressEventDetailScreenEvent.EndEvent -> {
                endEvent()
            }

            InProgressEventDetailScreenEvent.AnnouncementPublish -> {
                publishAnnouncement(_announcementText.value)
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

                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isLoadingLiveEventData = false,
                            liveEventData = result.data,
                            isNotPresent = false,
                            permissions = permissions
                        )
                        if (permissions.displayWorkers) {
                            loadAttendanceData()
                        }

                        if (databaseClient.selectUser().accountType == AccountType.HARVESTER.toString()) {
                            subscribeToAttendanceData()
                        }
                    }

                    is ResultHandler.Error -> {
                        if (result.error.name == DataError.NetworkError.NO_INTERNET.toString() && _inProgressEventDetailState.value.permissions!!.displayWorkers) {
                            loadAttendanceData()
                        } else {
                            if(result.error.name === DataError.NetworkError.NOT_FOUND.toString()){
                                _inProgressEventDetailState.value =
                                    _inProgressEventDetailState.value.copy(
                                        isNotPresent = true,
                                    )
                            } else {
                                _inProgressEventDetailState.value =
                                    _inProgressEventDetailState.value.copy(
                                        errorLiveEventData = result.error.asUiText().asNonCompString(),
                                        isNotPresent = false,
                                    )
                            }

                        }

                    }

                    is ResultHandler.Loading -> {
                        _inProgressEventDetailState.value =
                            _inProgressEventDetailState.value.copy(isLoadingLiveEventData = true)
                    }
                }
            }
        }
    }


    private fun onNewAnnouncement(announcement: AnnouncementItemWS) {
        _inProgressEventDetailState.update { currentState ->
            // Assuming liveEventData is nullable, hence the safe call (?)
            val currentAnnouncements = currentState.liveEventData?.announcementItems ?: emptyList()

            val updatedAnnouncements = currentAnnouncements + AnnouncementItemDto(
                createdAt = announcement.createdAt,
                message = announcement.message,
                id = "",
                eventID = "",
                userID = ""
            )

            // Update the state with the new list of announcements
            currentState.copy(
                liveEventData = currentState.liveEventData?.copy(
                    announcementItems = updatedAnnouncements
                )
            )
        }
    }


    private fun subscribeToAttendanceData() {
        this@InProgressEventDetailScreenComponent.coroutineScope().launch {
            subscribeToAnnouncementsUseCase(id, onNewAnnouncement = {
                onNewAnnouncement(it)
            }).collect { result ->

                when (result) {
                    is ResultHandler.Success -> {

                    }

                    is ResultHandler.Error -> {

                    }

                    is ResultHandler.Loading -> {

                    }
                }
            }
        }
    }

    private fun updateAttendance(attendance: AttendanceDataDto) {
        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
            isLoadingAttendanceUpdate = true
        )

        val dto = attendance.workers.map {
            AttendanceUpdateDto(
                userID = it.user.id,
                presenceStatus = it.presenceStatus,
                arrivedAt = if (it.arrivedAt != null) it.arrivedAt!!.toString() else null,
                leftAt = if (it.leftAt != null) it.leftAt!!.toString() else null,
            )
        }

        this@InProgressEventDetailScreenComponent.coroutineScope().launch {
            updateAttendanceUseCase(
                id, attendance = AttendanceUpdateListDto(workers = dto)
            ).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.deleteAttendance()
                        attendance.workers.forEach {
                            databaseClient.insertAttendanceItem(
                                it.user.id,
                                it.presenceStatus,
                                it.arrivedAt,
                                it.leftAt,
                                it.user.name
                            )
                        }
                        attendance.lastUpdated = Clock.System.now()
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isAttendanceUpdated = false,
                            attendanceData = attendance,
                            updatedAttendanceData = attendance,
                            isLoadingAttendanceUpdate = false,
                            isOffline = false
                        )
                    }

                    is ResultHandler.Error -> {

                        if (result.error == DataError.NetworkError.NO_INTERNET) {
                            try {
                                _inProgressEventDetailState.value.updatedAttendanceData!!.workers.forEach {
                                    databaseClient.updateAttendanceItem(
                                        userId = it.user.id,
                                        presenceStatus = it.presenceStatus,
                                        arrivedAt = it.arrivedAt,
                                        leftAt = it.leftAt,
                                    )
                                }

                                attendance.lastUpdated = Clock.System.now()
                                _inProgressEventDetailState.value =
                                    _inProgressEventDetailState.value.copy(
                                        isAttendanceUpdated = false,
                                        attendanceData = attendance,
                                        updatedAttendanceData = attendance,
                                        isLoadingAttendanceUpdate = false,
                                        isOffline = true
                                    )

                            } catch (e: Exception) {
                                println(e)
                            }
                        }

                        _inProgressEventDetailState.value =
                            _inProgressEventDetailState.value.copy(
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
                        var cacheLastUpdated: SelectLastUpdated? = null
                        try {
                            cacheLastUpdated = databaseClient.selectLastUpdated()
                        } catch (e: Exception) {
                            println(e)
                        }

                        if (cacheLastUpdated?.max != null && Instant.parse(cacheLastUpdated.max!!) > result.data.lastUpdated) {
                            val cachedAttendanceData = loadAttendanceFromCache(databaseClient)
                            updateAttendance(cachedAttendanceData)
                            _inProgressEventDetailState.value =
                                _inProgressEventDetailState.value.copy(
                                    isLoadingAttendanceData = false,
                                    attendanceData = cachedAttendanceData,
                                    updatedAttendanceData = cachedAttendanceData
                                )

                        } else {
                            try {
                                val cashedEvent = databaseClient.selectEvent()
                                if (cashedEvent.id != id) {
                                    databaseClient.insertEvent(
                                        id = id,
                                        userName = databaseClient.selectUser().name,
                                        name = _inProgressEventDetailState.value.liveEventData!!.name
                                    )
                                }
                            } catch (_: Exception) {
                                databaseClient.insertEvent(
                                    id = id,
                                    userName = databaseClient.selectUser().name,
                                    name = _inProgressEventDetailState.value.liveEventData!!.name
                                )
                            }

                            try {
                                databaseClient.deleteAttendance()
                                result.data.workers.forEach {
                                    databaseClient.insertAttendanceItem(
                                        it.user.id,
                                        it.presenceStatus,
                                        it.arrivedAt,
                                        it.leftAt,
                                        it.user.name
                                    )
                                }
                            } catch (_: Exception) {

                            }

                            _inProgressEventDetailState.value =
                                _inProgressEventDetailState.value.copy(
                                    isLoadingAttendanceData = false,
                                    attendanceData = result.data,
                                    updatedAttendanceData = result.data
                                )
                        }
                    }

                    is ResultHandler.Error -> {
                        if (result.error.name == DataError.NetworkError.NO_INTERNET.toString()) {

                            try {
                                val attendanceData = loadAttendanceFromCache(databaseClient)

                                _inProgressEventDetailState.value =
                                    _inProgressEventDetailState.value.copy(
                                        isOffline = true,
                                        isLoadingAttendanceData = false,
                                        attendanceData = attendanceData,
                                        updatedAttendanceData = attendanceData,
                                    )
                            } catch (e: Exception) {
                                println(e)
                            }

                        } else {
                            _inProgressEventDetailState.value =
                                _inProgressEventDetailState.value.copy(
                                    errorAttendanceData = result.error.asUiText().asNonCompString(),
                                )
                        }

                    }

                    is ResultHandler.Loading -> {
                        _inProgressEventDetailState.value =
                            _inProgressEventDetailState.value.copy(isLoadingAttendanceData = true)
                    }
                }
            }
        }
    }

    private fun endEvent() {
        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
            isLoadingEventEnd = true
        )
        this@InProgressEventDetailScreenComponent.coroutineScope().launch {
            endEventUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        databaseClient.deleteAttendance()
                        databaseClient.deleteEvent()
                        onNavigateBack()
                    }

                    is ResultHandler.Error -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            errorAttendanceData = result.error.asUiText().asNonCompString(),
                            isLoadingEventEnd = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isLoadingEventEnd = true
                        )
                    }
                }
            }
        }
    }

    private fun publishAnnouncement(message: String) {
        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
            isLoadingPublish = true
        )

        this@InProgressEventDetailScreenComponent.coroutineScope().launch {
            publishAnnouncementUseCase(id, message).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        onNewAnnouncement(
                            AnnouncementItemWS(
                                message = message,
                                createdAt = Clock.System.now()
                            )
                        )
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isLoadingPublish = false
                        )
                        _announcementText.value = ""
                    }

                    is ResultHandler.Error -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            errorAttendanceData = result.error.asUiText().asNonCompString(),
                            isLoadingPublish = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _inProgressEventDetailState.value = _inProgressEventDetailState.value.copy(
                            isLoadingPublish = true
                        )
                    }
                }
            }
        }
    }
}

fun loadAttendanceFromCache(databaseClient: SqlDelightDatabaseClient): AttendanceDataDto {
    val cachedWorkers: MutableList<AttendanceWorkerDto> =
        mutableListOf<AttendanceWorkerDto>()
    val cachedInDB = databaseClient.selectAttendanceItems()

    cachedInDB.forEach { item ->
        val user =
            AttendanceUserDto(id = item.userID, name = item.name ?: "")
        cachedWorkers.add(
            AttendanceWorkerDto(
                arrivedAt = if (item.arrivedAt != null && item.arrivedAt != "null") Instant.parse(
                    item.arrivedAt
                ) else null,
                leftAt = if (item.leftAt != null && item.leftAt != "null") Instant.parse(
                    item.leftAt
                ) else null,
                presenceStatus = convertToPresenceStatus(item.presenceStatus)!!,
                user = user,
                assignmentStatus = AssignmentStatus.ACTIVE
            )
        )
    }
    val lastUpdated = databaseClient.selectLastUpdated()
    return AttendanceDataDto(
        workers = cachedWorkers,
        lastUpdated = Instant.parse(lastUpdated.max!!)
    )
}