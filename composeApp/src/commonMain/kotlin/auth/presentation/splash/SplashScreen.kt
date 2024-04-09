package auth.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import auth.presentation.splash.component.SplashScreenComponent
import core.presentation.components.cicrular_progress.CustomCircularProgress
import core.presentation.components.logo.GrabItLogo

@Composable
fun SplashScreen(component: SplashScreenComponent) {
    LaunchedEffect(true) {
        component.verifyUserToken()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GrabItLogo(modifier = Modifier.align(Alignment.Center))
        CustomCircularProgress(
            modifier = Modifier.align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        )
    }
}