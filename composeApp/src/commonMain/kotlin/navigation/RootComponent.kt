package navigation

import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.register.component.RegisterStep1ScreenComponent
import auth.presentation.register.component.RegisterStep2ScreenComponent
import auth.presentation.register.component.RegisterStep3ScreenComponent
import auth.presentation.register.component.RegisterStepFinalScreenComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.LoginScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(config: Configuration, context: ComponentContext): Child {
        return when (config) {
            Configuration.LoginScreen -> Child.LoginScreenChild(
                LoginScreenComponent(
                    componentContext = context,
                    onNavigateToRegisterScreen = { email ->
                        navigation.pushNew(
                            Configuration.RegisterStep1Screen(
                                email
                            )
                        )
                    })
            )

            is Configuration.RegisterStep1Screen -> Child.RegisterStep1ScreenChild(
                RegisterStep1ScreenComponent(
                    email = config.email,
                    componentContext = context,
                    onNavigateBackToLoginScreen = {
                        navigation.pop()
                    },
                    onNavigateToRegisterStep2Screen = {
                        navigation.pushNew(Configuration.RegisterStep2Screen)
                    })
            )

            is Configuration.RegisterStep2Screen -> Child.RegisterStep2ScreenChild(
                RegisterStep2ScreenComponent(
                    componentContext = context,
                    onNavigateToRegisterStep3Screen = {
                        navigation.pushNew(Configuration.RegisterStep3Screen)
                    })
            )

            is Configuration.RegisterStep3Screen -> Child.RegisterStep3ScreenChild(
                RegisterStep3ScreenComponent(
                    componentContext = context,
                    onNavigateToRegisterStepFinalScreen = {
                        navigation.pushNew(Configuration.RegisterStepFinalScreen)
                    })
            )

            is Configuration.RegisterStepFinalScreen -> Child.RegisterStepFinalScreenChild(
                RegisterStepFinalScreenComponent(componentContext = context)
            )
        }
    }

    sealed class Child {
        data class LoginScreenChild(val component: LoginScreenComponent) : Child()
        data class RegisterStep1ScreenChild(val component: RegisterStep1ScreenComponent) : Child()
        data class RegisterStep2ScreenChild(val component: RegisterStep2ScreenComponent) : Child()
        data class RegisterStep3ScreenChild(val component: RegisterStep3ScreenComponent) : Child()
        data class RegisterStepFinalScreenChild(val component: RegisterStepFinalScreenComponent) :
            Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object LoginScreen : Configuration()

        @Serializable
        data class RegisterStep1Screen(val email: String) : Configuration()

        @Serializable
        data object RegisterStep2Screen : Configuration()

        @Serializable
        data object RegisterStep3Screen : Configuration()

        @Serializable
        data object RegisterStepFinalScreen : Configuration()
    }
}