import account_detail.presentation.account_detail.AccountDetailScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import auth.presentation.login.LoginScreen
import auth.presentation.register.RegisterStep1Screen
import auth.presentation.register.RegisterStep2Screen
import auth.presentation.register.RegisterStep3Screen
import auth.presentation.register.RegisterStepFinalScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.MutableValue
import event_detail.presentation.event_create.EventCreateScreen
import event_detail.presentation.event_detail_worker.EventDetailScreen
import home_screen.presentation.HomeScreen
import navigation.RootComponent
import ui.theme.GrabItTheme

@Composable
fun App(root: RootComponent) {
    GrabItTheme {
        val appState: SnackbarDemoAppState = rememberSnackbarDemoAppState()
        Scaffold(
            scaffoldState = appState.scaffoldState
        ) {
            Box(Modifier.background(MaterialTheme.colors.background)){
                val childStack by root.childStack.subscribeAsState()
                Children(
                    stack = childStack,
                    animation = stackAnimation(animator = fade(), disableInputDuringAnimation = true)
                ) { child ->
                    when (val instance = child.instance) {
                        is RootComponent.Child.LoginScreenChild -> LoginScreen(instance.component)
                        is RootComponent.Child.RegisterStep1ScreenChild -> RegisterStep1Screen(instance.component)
                        is RootComponent.Child.RegisterStep2ScreenChild -> RegisterStep2Screen(instance.component)
                        is RootComponent.Child.RegisterStep3ScreenChild -> RegisterStep3Screen(instance.component)
                        is RootComponent.Child.RegisterStepFinalScreenChild -> RegisterStepFinalScreen(
                            instance.component
                        )

                        is RootComponent.Child.EventDetailScreenChild -> EventDetailScreen(instance.component)
                        is RootComponent.Child.EventCreateScreenChild -> EventCreateScreen(instance.component)
                        is RootComponent.Child.HomeScreenChild -> HomeScreen(instance.component)
                        is RootComponent.Child.AccountDetailChild -> AccountDetailScreen(instance.component)
                    }
                }
            }
        }

    }
}