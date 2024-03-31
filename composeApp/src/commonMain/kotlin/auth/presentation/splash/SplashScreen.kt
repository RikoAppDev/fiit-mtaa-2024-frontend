package auth.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import auth.presentation.splash.component.SplashScreenComponent
import core.presentation.components.logo.GrabItLogo

@Composable
fun SplashScreen(component: SplashScreenComponent) {
    LaunchedEffect(true) {
        component.verifyUserToken()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GrabItLogo()
    }
}