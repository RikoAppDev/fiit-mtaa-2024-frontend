package event_detail.presentation.event_detail_worker.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event_detail.domain.use_case.LoadEventDataUseCase
import event_detail.presentation.event_detail_worker.EventDetailState
import kotlinx.coroutines.launch

class EventDetailScreenComponent(
    componentContext: ComponentContext,
    private val loadEventDataUseCase: LoadEventDataUseCase,
    private val id: String,
    private val onNavigateBack: () -> Unit,
) : ComponentContext by componentContext {

    private val _stateEventDetail = MutableValue(
        EventDetailState(isLoading = false, eventDetail = null, error = null)
    )
    val stateEventDetail: Value<EventDetailState> = _stateEventDetail

    fun onEvent(event: EventDetailScreenEvent) {
        when (event) {
            is EventDetailScreenEvent.SignInForEvent -> handleSignInForEvent() // TODO
            EventDetailScreenEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    private fun handleSignInForEvent() {
        print("Wocap")
    }

    fun loadEventData() {
        this@EventDetailScreenComponent.coroutineScope().launch {
            loadEventDataUseCase(id).collect { result ->
                println(result)
                when (result) {
                    is ResultHandler.Success -> {
                        println(result.data)
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            isLoading = false,
                            eventDetail = result.data
                        )
                    }

                    is ResultHandler.Error -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(
                            error = result.error.asUiText().asNonCompString()
                        )
                    }

                    is ResultHandler.Loading -> {
                        _stateEventDetail.value = _stateEventDetail.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
}