package auth.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import auth.presentation.splash.component.SplashScreenComponent
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import core.presentation.components.cicrular_progress.CustomCircularProgress
import core.presentation.components.logo.GrabItLogo
import dev.icerock.moko.biometry.compose.BindBiometryAuthenticatorEffect
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory

@Composable
fun SplashScreen(component: SplashScreenComponent) {
    val splashState by component.splashState.subscribeAsState()

    val biometryFactory = rememberBiometryAuthenticatorFactory()
    val biometryAuthenticator = biometryFactory.createBiometryAuthenticator()

    BindBiometryAuthenticatorEffect(biometryAuthenticator)

    LaunchedEffect(true) {
        if (biometryAuthenticator.isBiometricAvailable())
            component.verifyUserToken(biometryAuthenticator, true)
        else
            component.verifyUserToken(biometryAuthenticator, false)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GrabItLogo(modifier = Modifier.align(Alignment.Center))
        if (splashState.isLoading) {
            CustomCircularProgress(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp)
            )
        }
    }
}