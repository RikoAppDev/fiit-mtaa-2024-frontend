package event.presentation.reporting.component

import account_detail.domain.model.UpdateUser
import account_detail.domain.use_case.LogOutUseCase
import account_detail.domain.use_case.UpdateUserUseCase
import account_detail.presentation.account_detail.component.AccountDetailScreenEvent
import auth.domain.use_case.DeleteAccountUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import core.data.database.SqlDelightDatabaseClient
import core.domain.ResultHandler
import core.presentation.error_string_mapper.asUiText
import event.domain.use_case.LoadReportingUseCase
import event.presentation.event_detail.EventDetailState
import event.presentation.reporting.ReportingState
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.account_detail__success
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

class EventReportingScreenComponent(
    componentContext: ComponentContext,
    databaseClient: SqlDelightDatabaseClient,
    private val onNavigateBack: () -> Unit,
    private val id: String,
    private val loadReportingUseCase: LoadReportingUseCase,
) : ComponentContext by componentContext {

    private val _stateReporting = MutableValue(
        ReportingState(
            isLoading = true,
            reporting = null,
            user = databaseClient.selectUser()
        )
    )
    val stateReporting: Value<ReportingState> = _stateReporting


    fun onEvent(event: EventReportingScreenEvent) {
        when (event) {
            EventReportingScreenEvent.OnNavigateBack -> {
                onNavigateBack()
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    fun loadReporting() {
        this@EventReportingScreenComponent.coroutineScope().launch {
            loadReportingUseCase(id).collect { result ->
                when (result) {
                    is ResultHandler.Success -> {
                        _stateReporting.value = _stateReporting.value.copy(
                            isLoading = false,
                            reporting = result.data
                        )
                    }

                    is ResultHandler.Error -> {

                    }

                    is ResultHandler.Loading -> {
                        _stateReporting.value = _stateReporting.value.copy(
                            isLoading = true,
                        )
                    }
                }
            }
        }
    }

}