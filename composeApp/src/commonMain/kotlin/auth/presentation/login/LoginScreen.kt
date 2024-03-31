package auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import auth.presentation.login.component.LoginScreenComponent
import auth.presentation.login.component.LoginScreenEvent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.button_primary.ButtonPrimary
import core.presentation.components.filled_input.FilledInput
import core.presentation.components.logo.GrabItLogo
import core.presentation.components.snackbar.CustomSnackbar
import core.presentation.components.snackbar.SnackbarVisualWithError
import grabit.composeapp.generated.resources.Res
import grabit.composeapp.generated.resources.email
import grabit.composeapp.generated.resources.login_screen__create_account
import grabit.composeapp.generated.resources.login_screen__login
import grabit.composeapp.generated.resources.login_screen__no_account
import grabit.composeapp.generated.resources.password
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.domain.ColorVariation
import ui.theme.LightOnOrange

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreen(component: LoginScreenComponent) {
    val stateLogin = component.stateLogin.subscribeAsState()
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isVisible = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = {
                CustomSnackbar(
                    data = SnackbarVisualWithError(
                        snackbarData = it,
                        isError = true,
                    )
                )
            })
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            GrabItLogo()
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                color = MaterialTheme.colors.onBackground,
                text = stringResource(Res.string.login_screen__login),
                style = MaterialTheme.typography.h2
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FilledInput(
                    value = stateLogin.value.email,
                    onValueChange = { component.onEvent(LoginScreenEvent.UpdateEmail(it)) },
                    label = stringResource(Res.string.email),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                )
                FilledInput(
                    value = stateLogin.value.password,
                    onValueChange = { component.onEvent(LoginScreenEvent.UpdatePassword(it)) },
                    label = stringResource(Res.string.password),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    }
                )
                ButtonPrimary(
                    type = ColorVariation.ORANGE,
                    onClick = {
                        focusManager.clearFocus()
                        component.onEvent(LoginScreenEvent.ClickLoginButton)
                    },
                    text = stringResource(Res.string.login_screen__login)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.login_screen__no_account),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ClickableText(
                        text = AnnotatedString(stringResource(Res.string.login_screen__create_account)),
                        onClick = { component.onEvent(LoginScreenEvent.ClickRegisterButton) },
                        style = TextStyle(
                            color = LightOnOrange,
                            fontSize = MaterialTheme.typography.body2.fontSize,
                            fontWeight = MaterialTheme.typography.body2.fontWeight
                        ),
                    )
                }
            }
        }

        if (!isVisible.value && stateLogin.value.dataError != null) {
            coroutineScope.launch {
                isVisible.value = true
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = stateLogin.value.dataError!!,
                    duration = SnackbarDuration.Short
                )

                when (snackbarResult) {
                    SnackbarResult.Dismissed -> {
                        isVisible.value = false
                        component.onEvent(LoginScreenEvent.RemoveError)
                    }

                    SnackbarResult.ActionPerformed -> {
                        isVisible.value = false
                        component.onEvent(LoginScreenEvent.RemoveError)
                    }
                }
            }
        }
        if (!isVisible.value && stateLogin.value.emailError != null) {
            coroutineScope.launch {
                isVisible.value = true
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = stateLogin.value.emailError!!,
                    duration = SnackbarDuration.Short
                )

                when (snackbarResult) {
                    SnackbarResult.Dismissed -> {
                        isVisible.value = false
                        component.onEvent(LoginScreenEvent.RemoveError)
                    }

                    SnackbarResult.ActionPerformed -> {
                        isVisible.value = false
                        component.onEvent(LoginScreenEvent.RemoveError)
                    }
                }
            }
        }
        if (!isVisible.value && stateLogin.value.passwordError != null) {
            coroutineScope.launch {
                isVisible.value = true
                val snackbarResult = snackbarHostState.showSnackbar(
                    message = stateLogin.value.passwordError!!,
                    duration = SnackbarDuration.Short
                )

                when (snackbarResult) {
                    SnackbarResult.Dismissed -> {
                        isVisible.value = false
                        component.onEvent(LoginScreenEvent.RemoveError)
                    }

                    SnackbarResult.ActionPerformed -> {
                        isVisible.value = false
                        component.onEvent(LoginScreenEvent.RemoveError)
                    }
                }
            }
        }
    }
}