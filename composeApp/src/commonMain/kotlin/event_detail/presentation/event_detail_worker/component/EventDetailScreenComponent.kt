package event_detail.presentation.event_detail_worker.component

import auth.presentation.login.component.LoginScreenEvent
import com.arkivanov.decompose.ComponentContext

class EventDetailScreenComponent(componentContext: ComponentContext):ComponentContext by componentContext {
    fun onEvent(event: EventDetailScreenEvent){
        when (event){
            is EventDetailScreenEvent.SignInForEvent -> handleSignInForEvent() // TODO
        }

    }

    fun handleSignInForEvent(){
        print("Wocap")
    }
}