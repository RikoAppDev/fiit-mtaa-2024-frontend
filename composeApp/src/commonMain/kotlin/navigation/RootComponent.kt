package navigation

import account_detail.presentation.account_detail.component.AccountDetailComponent
import auth.domain.model.NewUser
import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.register.component.RegisterStep1ScreenComponent
import auth.presentation.register.component.RegisterStep2ScreenComponent
import auth.presentation.register.component.RegisterStep3ScreenComponent
import auth.presentation.register.component.RegisterStepFinalScreenComponent
import auth.presentation.splash.component.SplashScreenComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import core.data.database.SqlDelightDatabaseClient
import core.data.remote.KtorClient
import event_detail.presentation.event_create.component.EventCreateScreenComponent
import event_detail.presentation.event_detail_worker.component.EventDetailScreenComponent
import home_screen.presentation.component.HomeScreenComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    private val networkClient = KtorClient
    private val databaseClient = SqlDelightDatabaseClient

    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.SplashScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(config: Configuration, context: ComponentContext): Child {
        return when (config) {
            is Configuration.SplashScreen -> Child.SplashScreenChild(
                SplashScreenComponent(
                    componentContext = context,
                    networkClient = networkClient,
                    databaseClient = databaseClient,
                    onForkNavigateToApp = {
                        when (it) {
                            true -> navigation.replaceAll(Configuration.HomeScreen)
                            false -> navigation.replaceAll(Configuration.LoginScreen)
                        }
                    }
                )
            )

            is Configuration.LoginScreen -> Child.LoginScreenChild(
                LoginScreenComponent(
                    componentContext = context,
                    networkClient = networkClient,
                    databaseClient = databaseClient,
                    onNavigateToApplication = {
                        navigation.replaceAll(Configuration.HomeScreen)
                    },
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
                        navigation.pushNew(Configuration.RegisterStep2Screen(it))
                    })
            )

            is Configuration.RegisterStep2Screen -> Child.RegisterStep2ScreenChild(
                RegisterStep2ScreenComponent(
                    newUser = config.newUser,
                    componentContext = context,
                    onNavigateToRegisterStep3Screen = {
                        navigation.pushNew(Configuration.RegisterStep3Screen(it))
                    })
            )

            is Configuration.RegisterStep3Screen -> Child.RegisterStep3ScreenChild(
                RegisterStep3ScreenComponent(
                    newUser = config.newUser,
                    componentContext = context,
                    networkClient = networkClient,
                    databaseClient = databaseClient,
                    onNavigateToRegisterStepFinalScreen = {
                        navigation.replaceAll(Configuration.RegisterStepFinalScreen)
                    })
            )

            is Configuration.RegisterStepFinalScreen -> Child.RegisterStepFinalScreenChild(
                RegisterStepFinalScreenComponent(
                    componentContext = context,
                    onNavigateToApplication = {
                        navigation.replaceAll(Configuration.HomeScreen)
                    })
            )

            is Configuration.EventDetailScreen -> Child.EventDetailScreenChild(
                EventDetailScreenComponent(
                    componentContext = context,
                    id = config.id,
                    onNavigateBack = {
                        navigation.pop()
                    },
                    networkClient = networkClient

                )
            )

            is Configuration.EventCreateScreen -> Child.EventCreateScreenChild(
                EventCreateScreenComponent(
                    componentContext = context
                )
            )

            is Configuration.HomeScreen -> Child.HomeScreenChild(
                HomeScreenComponent(
                    componentContext = context,
                    onNavigateToAccountDetailScreen = {
                        navigation.pushNew(
                            Configuration.AccountDetail
                        )
                    },
                    networkClient = networkClient,
                    onNavigateToEventDetailScreen = {
                        navigation.pushNew(
                            Configuration.EventDetailScreen(it)
                        )
                    }
                )
            )

            is Configuration.AccountDetail -> Child.AccountDetailChild(
                AccountDetailComponent(
                    componentContext = context,
                    onNavigateBack = {
                        navigation.pop()
                    },
                    onNavigateToLoginScreen = {
                        navigation.replaceAll(Configuration.LoginScreen)
                    },
                    databaseClient = databaseClient,
                    networkClient = networkClient
                )
            )
        }
    }

    sealed class Child {
        data class SplashScreenChild(val component: SplashScreenComponent) : Child()
        data class LoginScreenChild(val component: LoginScreenComponent) : Child()
        data class RegisterStep1ScreenChild(val component: RegisterStep1ScreenComponent) : Child()
        data class RegisterStep2ScreenChild(val component: RegisterStep2ScreenComponent) : Child()
        data class RegisterStep3ScreenChild(val component: RegisterStep3ScreenComponent) : Child()
        data class RegisterStepFinalScreenChild(val component: RegisterStepFinalScreenComponent) :
            Child()

        data class EventDetailScreenChild(val component: EventDetailScreenComponent) : Child()
        data class EventCreateScreenChild(val component: EventCreateScreenComponent) : Child()

        data class HomeScreenChild(val component: HomeScreenComponent) : Child()

        data class AccountDetailChild(val component: AccountDetailComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object SplashScreen : Configuration()

        @Serializable
        data object LoginScreen : Configuration()

        @Serializable
        data class RegisterStep1Screen(val email: String) : Configuration()

        @Serializable
        data class RegisterStep2Screen(val newUser: NewUser) : Configuration()

        @Serializable
        data class RegisterStep3Screen(val newUser: NewUser) : Configuration()

        @Serializable
        data object RegisterStepFinalScreen : Configuration()

        @Serializable
        data class EventDetailScreen(val id: String) : Configuration()

        @Serializable
        data object EventCreateScreen : Configuration()

        @Serializable
        data object HomeScreen : Configuration()

        @Serializable
        data object AccountDetail : Configuration()


    }
}