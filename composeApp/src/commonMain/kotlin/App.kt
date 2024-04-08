import account_detail.presentation.account_detail.AccountDetailScreen
import all_events_screen.presentation.AllEventsScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import auth.presentation.login.LoginScreen
import auth.presentation.register.RegisterStep1Screen
import auth.presentation.register.RegisterStep2Screen
import auth.presentation.register.RegisterStep3Screen
import auth.presentation.register.RegisterStepFinalScreen
import auth.presentation.splash.SplashScreen
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import event.presentation.create_update.EventCreateUpdateScreen
import event.presentation.event_detail.EventDetailScreen
import event.presentation.event_detail_live.InProgressEventDetailScreen
import events_on_map_screen.EventsOnMapScreen
import home_screen.presentation.HomeScreen
import navigation.RootComponent
import ui.theme.GrabItTheme

@Composable
fun App(root: RootComponent) {
    GrabItTheme {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            val childStack by root.childStack.subscribeAsState()
            Children(
                stack = childStack,
                animation = stackAnimation(animator = fade(), disableInputDuringAnimation = true)
            ) { child ->

                when (val instance = child.instance) {
                    is RootComponent.Child.SplashScreenChild -> SplashScreen(instance.component)
                    is RootComponent.Child.LoginScreenChild -> LoginScreen(instance.component)
                    is RootComponent.Child.RegisterStep1ScreenChild -> RegisterStep1Screen(instance.component)
                    is RootComponent.Child.RegisterStep2ScreenChild -> RegisterStep2Screen(instance.component)
                    is RootComponent.Child.RegisterStep3ScreenChild -> RegisterStep3Screen(instance.component)
                    is RootComponent.Child.RegisterStepFinalScreenChild -> RegisterStepFinalScreen(
                        instance.component
                    )

                    is RootComponent.Child.EventDetailScreenChild -> EventDetailScreen(instance.component)
                    is RootComponent.Child.EventCreateScreenChild -> EventCreateUpdateScreen(instance.component)
                    is RootComponent.Child.HomeScreenChild -> HomeScreen(instance.component)
                    is RootComponent.Child.AccountDetailChild -> AccountDetailScreen(instance.component)
                    is RootComponent.Child.EventsOnMapScreenChild -> EventsOnMapScreen(instance.component)
                    is RootComponent.Child.AllEventsScreenChild -> AllEventsScreen(instance.component)
                    is RootComponent.Child.InProgressEventDetailScreenChild -> InProgressEventDetailScreen(instance.component)
                }
            }
        }
    }
}