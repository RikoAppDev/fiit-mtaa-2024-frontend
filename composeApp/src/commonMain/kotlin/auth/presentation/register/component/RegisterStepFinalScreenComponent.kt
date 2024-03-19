package auth.presentation.register.component

import com.arkivanov.decompose.ComponentContext

class RegisterStepFinalScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToApplication: () -> Unit
) : ComponentContext by componentContext {
    fun startUsingApp() = onNavigateToApplication()
}