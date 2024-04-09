package event.presentation.my.component

import all_events_screen.domain.use_case.LoadCategoriesWithCountUseCase
import all_events_screen.domain.use_case.LoadFilteredEventsUseCase
import all_events_screen.presentation.AllEventsState
import all_events_screen.presentation.component.AllEventScreenEvent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.grabit.GrabItDatabase
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.domain.use_case.LoadMyEventsUseCase
import event.presentation.my.MyEventsState
import kotlinx.coroutines.launch
import navigation.BottomNavigationEvent

class MyEventsScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccountDetailScreen: () -> Unit,
    private val onNavigateBottomBarItem: (BottomNavigationEvent) -> Unit,
    private val navigateToEventDetailScreen: (id: String) -> Unit,
    database: SqlDelightDatabaseClient,
    private val loadMyEventsUseCase: LoadMyEventsUseCase

) : ComponentContext by componentContext {
    private val _myEventsState = MutableValue(
        MyEventsState(
            isLoadingEvents = true,
            errorEvents = null,
            events = null,
        )
    )
    val myEventsState: Value<MyEventsState> = _myEventsState

    private val _accountType = MutableValue(
        database.selectUser().accountType
    )

    val accountType: Value<String> = _accountType

    fun onEvent(event: MyEventsScreenEvent) {
        when (event) {
            is MyEventsScreenEvent.NavigateBottomBarItem -> {
                onNavigateBottomBarItem(event.navigationEvent)
            }

            is MyEventsScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }

            is MyEventsScreenEvent.NavigateToEventDetail -> {
                navigateToEventDetailScreen(event.id)
            }

            is MyEventsScreenEvent.RemoveError -> {
                _myEventsState.value = _myEventsState.value.copy(
                    errorEvents = null
                )
            }
        }
    }

    fun loadMyEvents() {
        this@MyEventsScreenComponent.coroutineScope().launch {
            loadMyEventsUseCase().collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _myEventsState.value = _myEventsState.value.copy(
                            isLoadingEvents = false,
                            events = result.data
                        )
                    }

                    is ResultHandler.Error -> {
                        _myEventsState.value = _myEventsState.value.copy(
                            errorEvents = result.error.asUiText().asNonCompString(),
                            isLoadingEvents = false
                        )
                    }

                    is ResultHandler.Loading -> {
                        _myEventsState.value = _myEventsState.value.copy(
                            isLoadingEvents = true
                        )
                    }
                }
            }
        }
    }


}