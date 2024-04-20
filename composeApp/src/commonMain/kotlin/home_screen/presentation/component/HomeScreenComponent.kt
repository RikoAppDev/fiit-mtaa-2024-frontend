package home_screen.presentation.component

import auth.domain.model.AccountType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.DataError
import core.domain.GpsPosition
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import dev.icerock.moko.geo.LocationTracker
import event.domain.use_case.LoadAttendanceDataUseCase
import home_screen.data.User
import home_screen.data.ActiveEventDto
import home_screen.domain.use_case.GetActiveEventUseCase
import home_screen.domain.use_case.GetLatestEventsUseCase
import home_screen.domain.use_case.GetNearestEventsUseCase
import home_screen.presentation.HomescreenState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import navigation.BottomNavigationEvent

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val databaseClient: SqlDelightDatabaseClient,
    private val getLatestEventsUseCase: GetLatestEventsUseCase,
    private val getNearestEventsUseCase: GetNearestEventsUseCase,
    private val getActiveEventUseCase: GetActiveEventUseCase,
    private val loadAttendanceDataUseCase: LoadAttendanceDataUseCase,
    private val onNavigateToInProgressEventScreen: (id: String) -> Unit,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateToEventDetailScreen: (id: String) -> Unit,
    val user: com.grabit.User,
) : ComponentContext by componentContext {
    private val _actualLocation = MutableValue(GpsPosition(null, null))
    val actualLocation: Value<GpsPosition> = _actualLocation

    private val _homeScreenState = MutableValue(
        HomescreenState(
            isLatestEventsLoading = true,
            isOffline = false,
            isNearestEventsLoading = false,
            isActiveEventLoading = true,
            latestEvents = null,
            nearestEvents = null,
            activeEvent = null,
            error = "",
        )
    )
    val homeScreenState: Value<HomescreenState> = _homeScreenState


    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }

            is HomeScreenEvent.NavigateToEventDetailScreen -> {
                onNavigateToEventDetailScreen(event.id)
            }

            is HomeScreenEvent.NavigateBottomBarItem -> {
                onNavigateBottomBarItem(event.navigationEvent)
            }

            is HomeScreenEvent.RemoveError -> {
                _homeScreenState.value = _homeScreenState.value.copy(
                    error = ""
                )
            }

            is HomeScreenEvent.NavigateToActiveEvent -> {
                onNavigateToInProgressEventScreen(event.id)
            }
        }
    }

    fun initLocationTracker(locationTracker: LocationTracker) {
        locationTracker.getLocationsFlow().onEach {
            try {
                if (_actualLocation.value.latitude != null && _actualLocation.value.longitude != null) {
                    _actualLocation.value = GpsPosition(it.latitude, it.longitude)
                } else {
                    _actualLocation.value = GpsPosition(it.latitude, it.longitude)
                    loadNearestEvents()
                }
            } catch (e: Exception) {
                println("Error: $e")
            }
            println("Location: ${actualLocation.value.latitude}, ${actualLocation.value.longitude}")
        }.launchIn(coroutineScope())
    }

    fun startLocationTracking(locationTracker: LocationTracker) {
        coroutineScope().launch {
            try {
                locationTracker.startTracking()
            } catch (e: Exception) {
                println("Error: $e")
            }
        }
    }

    fun loadLatestEvents() {
        this@HomeScreenComponent.coroutineScope().launch {
            getLatestEventsUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isLatestEventsLoading = false,
                            latestEvents = result.data,
                            error = "",
                        )
                    }

                    is ResultHandler.Error -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isLatestEventsLoading = false,
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isLatestEventsLoading = true,
                        )
                    }
                }
            }
        }
    }

    fun loadNearestEvents() {
        if (_actualLocation.value.latitude == null || _actualLocation.value.longitude == null) return
        coroutineScope().launch {
            getNearestEventsUseCase(_actualLocation.value).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isNearestEventsLoading = false,
                            nearestEvents = result.data,
                            error = "",
                        )
                    }

                    is ResultHandler.Error -> {
                        if (result.error == DataError.NetworkError.NOT_FOUND) {
                            _homeScreenState.value = _homeScreenState.value.copy(
                                isNearestEventsLoading = false,
                                nearestEvents = null,
                                error = "",
                            )
                        } else {
                            _homeScreenState.value = _homeScreenState.value.copy(
                                isNearestEventsLoading = false,
                                error = result.error.asUiText().asNonCompString()
                            )
                        }
                    }

                    is ResultHandler.Loading -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isNearestEventsLoading = true,
                        )
                    }
                }
            }
        }
    }

    fun getActiveEvent() {
        this@HomeScreenComponent.coroutineScope().launch {
            getActiveEventUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isActiveEventLoading = false,
                            activeEvent = result.data,
                            error = "",
                            isOffline = false
                        )
                        if (user.accountType == AccountType.ORGANISER.toString()) {
                            try {
                                val cashedEvent = databaseClient.selectEvent()
                                if (cashedEvent.id != result.data.id) {
                                    databaseClient.insertEvent(
                                        result.data.id,
                                        result.data.user.name,
                                        result.data.name
                                    )
                                }
                            } catch (_: Exception) {
                                databaseClient.insertEvent(
                                    result.data.id,
                                    result.data.user.name,
                                    result.data.name
                                )
                            }
                        }
                    }

                    is ResultHandler.Error -> {
                        if (result.error.name == "NO_INTERNET" && user.accountType == AccountType.ORGANISER.toString()) {
                            _homeScreenState.value = _homeScreenState.value.copy(
                                isOffline = true,
                            )
                            try {
                                val eventInCache = databaseClient.selectEvent()
                                _homeScreenState.value = _homeScreenState.value.copy(
                                    isActiveEventLoading = false,
                                    activeEvent = ActiveEventDto(
                                        id = eventInCache.id,
                                        name = eventInCache.name,
                                        thumbnailURL = "",
                                        user = User(
                                            name = eventInCache.userName
                                        )
                                    ),
                                    error = ""
                                )
                            } catch (e: Exception) {
                                println(e)
                            }

                        } else {
                            databaseClient.deleteEvent()
                            databaseClient.deleteAttendance()
                            _homeScreenState.value = _homeScreenState.value.copy(
                                isActiveEventLoading = false,
                                activeEvent = null,
                                isOffline = false,
                                error = if (!result.error.name.equals("NOT_FOUND")) result.error.asUiText()
                                    .asNonCompString() else ""
                            )
                        }
                    }

                    is ResultHandler.Loading -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isActiveEventLoading = true,
                        )
                    }
                }
            }
        }
    }
}
