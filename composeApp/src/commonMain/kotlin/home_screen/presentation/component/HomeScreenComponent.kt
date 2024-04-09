package home_screen.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.grabit.User
import core.data.remote.dto.EventCardDto
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import home_screen.domain.use_case.GetActiveEventUseCase
import home_screen.domain.use_case.GetLatestEventsUseCase
import home_screen.presentation.HomescreenState
import kotlinx.coroutines.launch
import navigation.BottomNavigationEvent

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val getLatestEventsUseCase: GetLatestEventsUseCase,
    private val getActiveEventUseCase: GetActiveEventUseCase,
    val onNavigateToInProgressEventScreen: (id: String) -> Unit,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateToEventDetailScreen: (id: String) -> Unit,
    val user: User
) : ComponentContext by componentContext {

    private val _homeScreenState = MutableValue(
        HomescreenState(
            isLatestEventsLoading = true,
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

    fun loadLatestEvents() {
        this@HomeScreenComponent.coroutineScope().launch {
            getLatestEventsUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isLatestEventsLoading = false,
                            latestEvents = result.data,
                            error = ""
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

    fun getLatestEvent() {
        this@HomeScreenComponent.coroutineScope().launch {
            getActiveEventUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isActiveEventLoading = false,
                            activeEvent = result.data,
                            error = ""
                        )
                    }

                    is ResultHandler.Error -> {
                        _homeScreenState.value = _homeScreenState.value.copy(
                            isActiveEventLoading = false,
                            error = if (!result.error.name.equals("NOT_FOUND")) result.error.asUiText()
                                .asNonCompString() else ""
                        )

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
