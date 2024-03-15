import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import auth.presentation.login.LoginScreen
import auth.presentation.register.RegisterStep1Screen
import auth.presentation.register.RegisterStep2Screen
import auth.presentation.register.RegisterStep3Screen
import auth.presentation.register.RegisterStepFinalScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import event_detail.presentation.event_detail_worker.EventDetailScreen
import navigation.RootComponent

@Composable
fun App(root: RootComponent) {
    MaterialTheme {
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
            }
        }
    }
}