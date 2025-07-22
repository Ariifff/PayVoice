package com.arif.payvoice.starter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.arif.payvoice.accessories.PreferenceHelper
import com.arif.payvoice.accessories.Routes
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.Purple80
import com.arif.payvoice.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(2000)
        if (PreferenceHelper.isOnboardingDone(context)) {
            navController.navigate(Routes.Main) {
                popUpTo(Routes.Splash) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.Onboarding) {
                popUpTo(Routes.Splash) { inclusive = true }
            }
        }
    }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Purple80, Blue))
            ),
        contentAlignment = Alignment.Center
    ) {
        val logoSize = maxWidth * 0.9f // responsive logo size


        Image(
            painter = painterResource(id = R.drawable.logo), // your logo here
            contentDescription = "App Logo",
            modifier = Modifier.size(logoSize)
        )
    }
}
