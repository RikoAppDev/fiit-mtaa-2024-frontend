package event.presentation.event_detail_live.component

import com.arkivanov.decompose.ComponentContext

class InProgressEventDetailScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit,
) : ComponentContext by componentContext {

    fun onEvent(event:InProgressEventDetailScreenEvent){

    }
}