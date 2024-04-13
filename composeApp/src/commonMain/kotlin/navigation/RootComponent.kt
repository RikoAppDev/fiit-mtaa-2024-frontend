package navigation

import account_detail.domain.use_case.UpdateUserUseCase
import account_detail.presentation.account_detail.component.AccountDetailComponent
import all_events_screen.domain.use_case.LoadCategoriesWithCountUseCase
import all_events_screen.domain.use_case.LoadFilteredEventsUseCase
import all_events_screen.presentation.component.AllEventScreenComponent
import auth.domain.AuthValidation
import auth.domain.model.NewUser
import auth.domain.use_case.DeleteAccountUseCase
import auth.domain.use_case.LoginUserUseCase
import auth.domain.use_case.RegisterUserUseCase
import auth.domain.use_case.VerifyTokenUseCase
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
import core.domain.NetworkHandler
import event.domain.use_case.CreateEventUseCase
import event.domain.use_case.LoadAttendanceDataUseCase
import event.domain.use_case.LoadEventDataUseCase
import event.presentation.create_update.component.EventCreateUpdateScreenComponent
import event.domain.use_case.LoadEventWorkersUseCase
import event.domain.use_case.LoadInProgressEventDataUseCase
import event.domain.use_case.LoadMyEventsUseCase
import event.domain.use_case.SignInForEventUseCase
import event.domain.use_case.SignOffEventUseCase
import event.domain.use_case.StartEventUseCase
import event.domain.use_case.UpdateEventUseCase
import event.domain.use_case.UploadImageUseCase
import event.presentation.event_detail.component.EventDetailScreenComponent
import event.presentation.event_detail_live.component.InProgressEventDetailScreenComponent
import event.presentation.my.component.MyEventsScreenComponent
import events_on_map_screen.domain.use_case.LoadPointsUseCase
import home_screen.domain.use_case.GetLatestEventsUseCase
import events_on_map_screen.presentation.component.EventsOnMapScreenComponent
import home_screen.domain.use_case.GetActiveEventUseCase
import home_screen.presentation.component.HomeScreenComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()
    private val networkClient = KtorClient
    private val networkHandler = NetworkHandler(networkClient)
    private val databaseClient = SqlDelightDatabaseClient

    // Splash screen
    private val verifyTokenUseCase = VerifyTokenUseCase(networkClient, databaseClient)

    // Login screen
    private val loginUserUseCase = LoginUserUseCase(networkHandler)
    private val authValidation = AuthValidation()

    // Register Step3 screen
    private val registerUserUseCase = RegisterUserUseCase(networkHandler)

    // Home screen
    private val getLatestEventsUseCase = GetLatestEventsUseCase(networkHandler, databaseClient)
    private val getActiveEventUseCase = GetActiveEventUseCase(networkHandler, databaseClient)

    // Account detail screen
    private val updateUserUseCase = UpdateUserUseCase(networkHandler)
    private val deleteAccountUseCase = DeleteAccountUseCase(networkHandler, databaseClient)

    // Events on map screen
    private val getMapPointsUseCase = LoadPointsUseCase(networkHandler, databaseClient)

    // Event detail screen
    private val loadEventDataUseCase = LoadEventDataUseCase(networkHandler, databaseClient)
    private val loadEventWorkersUseCase = LoadEventWorkersUseCase(networkHandler, databaseClient)
    private val signInForEventUseCase = SignInForEventUseCase(networkHandler, databaseClient)
    private val signOffEventUseCase = SignOffEventUseCase(networkHandler, databaseClient)
    private val startEventUseCase = StartEventUseCase(networkHandler, databaseClient)


    // All events screen
    private val loadCategoriesWithCountUseCase =
        LoadCategoriesWithCountUseCase(networkHandler, databaseClient)
    private val loadFilteredEventsUseCase =
        LoadFilteredEventsUseCase(networkHandler, databaseClient)


    // My events screen
    private val loadMyEventsUseCase = LoadMyEventsUseCase(networkHandler, databaseClient)


    // CreateUpdateEventScreen
    private val uploadImageUseCase = UploadImageUseCase(networkHandler, databaseClient)
    private val createEventUseCase = CreateEventUseCase(networkHandler, databaseClient)
    private val updateEventUseCase = UpdateEventUseCase(networkHandler, databaseClient)

    // InProgressEventScreen
    private val loadInProgressEventDataUseCase = LoadInProgressEventDataUseCase(networkHandler, databaseClient)
    private val loadAttendanceDataUseCase =
        LoadAttendanceDataUseCase(networkHandler, databaseClient)


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
                    verifyTokenUseCase = verifyTokenUseCase,
                    onForkNavigateToApp = { valid, error ->
                        when (valid) {
                            true -> navigation.replaceAll(Configuration.HomeScreen)
                            false -> navigation.replaceAll(Configuration.LoginScreen(error))
                        }
                    }
                )
            )

            is Configuration.LoginScreen -> Child.LoginScreenChild(
                LoginScreenComponent(
                    componentContext = context,
                    loginUserUseCase = loginUserUseCase,
                    authValidation = authValidation,
                    databaseClient = databaseClient,
                    error = config.error,
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
                    componentContext = context,
                    authValidation = authValidation,
                    email = config.email,
                    onNavigateBackToLoginScreen = {
                        navigation.pop()
                    },
                    onNavigateToRegisterStep2Screen = {
                        navigation.pushNew(Configuration.RegisterStep2Screen(it))
                    })
            )

            is Configuration.RegisterStep2Screen -> Child.RegisterStep2ScreenChild(
                RegisterStep2ScreenComponent(
                    componentContext = context,
                    newUser = config.newUser,
                    onNavigateToRegisterStep3Screen = {
                        navigation.pushNew(Configuration.RegisterStep3Screen(it))
                    })
            )

            is Configuration.RegisterStep3Screen -> Child.RegisterStep3ScreenChild(
                RegisterStep3ScreenComponent(
                    componentContext = context,
                    registerUserUseCase = registerUserUseCase,
                    databaseClient = databaseClient,
                    newUser = config.newUser,
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
                    databaseClient = databaseClient,
                    componentContext = context,
                    loadEventDataUseCase = loadEventDataUseCase,
                    loadEventWorkersUseCase = loadEventWorkersUseCase,
                    signInForEventUseCase = signInForEventUseCase,
                    signOffEventUseCase = signOffEventUseCase,
                    startEventUseCase = startEventUseCase,
                    id = config.id,
                    onNavigateBack = {
                        navigation.pop()
                    },

                    navigateToEditEvent = {
                        navigation.pushNew(Configuration.EventCreateUpdateScreen)
                    },
                    navigateToLiveEvent = {
                        navigation.replaceAll(
                            Configuration.HomeScreen,
                            Configuration.InProgressEventDetailScreen(it)
                        )
                    }
                )
            )

            is Configuration.EventCreateUpdateScreen -> Child.EventCreateScreenChild(
                EventCreateUpdateScreenComponent(
                    componentContext = context,
                    uploadImageUseCase = uploadImageUseCase,
                    createEventUseCase = createEventUseCase,
                    updateEventUseCase = updateEventUseCase,
                    onNavigateBack = {
                        navigation.pop()
                    }
                )
            )

            is Configuration.HomeScreen -> Child.HomeScreenChild(
                HomeScreenComponent(
                    user = databaseClient.selectUser(),
                    componentContext = context,
                    getLatestEventsUseCase = getLatestEventsUseCase,
                    getActiveEventUseCase = getActiveEventUseCase,
                    onNavigateToInProgressEventScreen = {
                        navigation.replaceAll(
                            Configuration.HomeScreen,
                            Configuration.InProgressEventDetailScreen(it)
                        )
                    },

                    onNavigateBottomBarItem = { event ->
                        when (event) {
                            BottomNavigationEvent.OnNavigateToHomeScreen -> {}
                            BottomNavigationEvent.OnNavigateToAllHarvestsScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.AllEventsScreen
                                )
                            }

                            BottomNavigationEvent.OnNavigateToMapScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.EventsOnMapScreen
                                )
                            }

                            BottomNavigationEvent.OnNavigateToMyHarvestScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.MyEventsScreen
                                )
                            }
                        }
                    },
                    onNavigateToAccountDetailScreen = {
                        navigation.pushNew(
                            Configuration.AccountDetail
                        )
                    },
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
                    updateUserUseCase = updateUserUseCase,
                    databaseClient = databaseClient,
                    deleteAccountUseCase = deleteAccountUseCase,
                    onNavigateBack = {
                        navigation.pop()
                    },
                    onNavigateToLoginScreen = {
                        navigation.replaceAll(Configuration.LoginScreen())
                    }
                )
            )

            is Configuration.EventsOnMapScreen -> Child.EventsOnMapScreenChild(
                EventsOnMapScreenComponent(
                    componentContext = context,
                    getMapPointsUseCase = getMapPointsUseCase,
                    loadEventDataUseCase = loadEventDataUseCase,
                    onNavigateToAccountDetailScreen = {
                        navigation.pushNew(
                            Configuration.AccountDetail
                        )
                    },
                    navigateToEventDetailScreen = {
                        navigation.pushNew(
                            Configuration.EventDetailScreen(it)
                        )
                    },
                    onNavigateBottomBarItem = { event ->
                        when (event) {
                            BottomNavigationEvent.OnNavigateToHomeScreen -> {
                                navigation.replaceAll(Configuration.HomeScreen)
                            }

                            BottomNavigationEvent.OnNavigateToAllHarvestsScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.AllEventsScreen
                                )
                            }

                            BottomNavigationEvent.OnNavigateToMapScreen -> {}

                            BottomNavigationEvent.OnNavigateToMyHarvestScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.MyEventsScreen
                                )
                            }
                        }
                    }
                )
            )

            is Configuration.AllEventsScreen -> Child.AllEventsScreenChild(
                AllEventScreenComponent(
                    componentContext = context,
                    loadCategoriesWithCountUseCase = loadCategoriesWithCountUseCase,
                    loadFilteredEventsUseCase = loadFilteredEventsUseCase,
                    onNavigateToAccountDetailScreen = {
                        navigation.pushNew(
                            Configuration.AccountDetail
                        )
                    },
                    onNavigateBottomBarItem = { event ->
                        when (event) {
                            BottomNavigationEvent.OnNavigateToHomeScreen -> {
                                navigation.replaceAll(Configuration.HomeScreen)
                            }

                            BottomNavigationEvent.OnNavigateToAllHarvestsScreen -> {}

                            BottomNavigationEvent.OnNavigateToMapScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.EventsOnMapScreen
                                )
                            }

                            BottomNavigationEvent.OnNavigateToMyHarvestScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.MyEventsScreen
                                )
                            }
                        }
                    },
                    navigateToEventDetailScreen = {
                        navigation.pushNew(
                            Configuration.EventDetailScreen(it)
                        )
                    }
                )
            )

            is Configuration.InProgressEventDetailScreen -> Child.InProgressEventDetailScreenChild(
                InProgressEventDetailScreenComponent(
                    componentContext = context,
                    id = config.id,
                    loadInProgressEventDataUseCase = loadInProgressEventDataUseCase,
                    loadAttendanceDataUseCase = loadAttendanceDataUseCase,
                    onNavigateBack = {
                        navigation.pop()
                    },
                    databaseClient = databaseClient
                )
            )

            is Configuration.MyEventsScreen -> Child.MyEventsScreenChild(
                MyEventsScreenComponent(
                    componentContext = context,
                    loadMyEventsUseCase = loadMyEventsUseCase,
                    database = databaseClient,
                    onNavigateToAccountDetailScreen = {
                        navigation.pushNew(
                            Configuration.AccountDetail
                        )
                    },
                    onNavigateBottomBarItem = { event ->
                        when (event) {
                            BottomNavigationEvent.OnNavigateToHomeScreen -> {
                                navigation.replaceAll(Configuration.HomeScreen)
                            }

                            BottomNavigationEvent.OnNavigateToAllHarvestsScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.AllEventsScreen
                                )
                            }

                            BottomNavigationEvent.OnNavigateToMapScreen -> {
                                navigation.replaceAll(
                                    Configuration.HomeScreen,
                                    Configuration.EventsOnMapScreen
                                )
                            }

                            BottomNavigationEvent.OnNavigateToMyHarvestScreen -> {}
                        }
                    },
                    navigateToEventDetailScreen = {
                        navigation.pushNew(
                            Configuration.EventDetailScreen(it)
                        )
                    },
                    onNavigateToCreateEventScreen = {
                        navigation.pushNew(Configuration.EventCreateUpdateScreen)
                    }
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
        data class EventCreateScreenChild(val component: EventCreateUpdateScreenComponent) : Child()

        data class HomeScreenChild(val component: HomeScreenComponent) : Child()

        data class AccountDetailChild(val component: AccountDetailComponent) : Child()

        data class EventsOnMapScreenChild(val component: EventsOnMapScreenComponent) : Child()

        data class AllEventsScreenChild(val component: AllEventScreenComponent) : Child()

        data class MyEventsScreenChild(val component: MyEventsScreenComponent) : Child()

        data class InProgressEventDetailScreenChild(val component: InProgressEventDetailScreenComponent) :
            Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object SplashScreen : Configuration()

        @Serializable
        data class LoginScreen(val error: String? = null) : Configuration()

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
        data object EventCreateUpdateScreen : Configuration()

        @Serializable
        data object HomeScreen : Configuration()

        @Serializable
        data object AccountDetail : Configuration()

        @Serializable
        data object EventsOnMapScreen : Configuration()

        @Serializable
        data object AllEventsScreen : Configuration()

        @Serializable
        data object MyEventsScreen : Configuration()

        @Serializable
        data class InProgressEventDetailScreen(val id: String) : Configuration()


    }
}