package home_screen.presentation.component

import com.arkivanov.decompose.ComponentContext


class HomeScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccountDetailScreen: () -> Unit,
) : ComponentContext by componentContext {


    fun onEvent(event:HomeScreenEvent){
        when(event){
            HomeScreenEvent.NavigateToAccountDetailScreen -> {
                onNavigateToAccountDetailScreen()
            }
        }
    }

}