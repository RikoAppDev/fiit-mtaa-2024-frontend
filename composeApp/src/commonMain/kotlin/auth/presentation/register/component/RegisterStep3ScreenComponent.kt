package auth.presentation.register.component

import auth.domain.model.NewUser
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.decompose.value.Value
import core.data.remote.KtorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class RegisterStep3ScreenComponent(
    val newUser: NewUser,
    componentContext: ComponentContext,
    private val ktorClient: KtorClient = KtorClient,
    private val onNavigateToRegisterStepFinalScreen: () -> Unit
) : ComponentContext by componentContext {
    private val _name = MutableValue("")
    val name: Value<String> = _name
    private val _phone = MutableValue("")
    val phone: Value<String> = _phone

    fun onEvent(event: RegisterStep3ScreenEvent) {
        when (event) {
            is RegisterStep3ScreenEvent.UpdateName -> {
                _name.value = event.name
            }

            is RegisterStep3ScreenEvent.UpdatePhone -> {
                _phone.value = event.phone
            }

            is RegisterStep3ScreenEvent.ClickCreateAccountButton -> {
                createAccount(newUser)
                //onNavigateToRegisterStepFinalScreen()
            }
        }
    }

    private fun createAccount(newUser: NewUser) {
        this@RegisterStep3ScreenComponent.coroutineScope(Dispatchers.IO).launch {
            ktorClient.registerUser(newUser)
        }
    }
}