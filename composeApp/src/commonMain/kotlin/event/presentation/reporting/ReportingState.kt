package event.presentation.reporting

import com.grabit.User
import event.presentation.reporting.data.dto.ReportingItemsListDto

data class ReportingState (
    val isLoading:Boolean,
    val reporting:ReportingItemsListDto?,
    val user:User
)