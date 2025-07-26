package com.arif.payvoice.starter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arif.payvoice.accessories.Routes
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.Purple80
import com.arif.payvoice.R
import com.arif.payvoice.util.isPermissionShown
import com.arif.payvoice.util.setPermissionShown
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(1500) // splash delay
        val permissionGranted = isNotificationServiceEnabled(context)
        val alreadyShownOnce = context.isPermissionShown()

        when {
            permissionGranted -> {
                context.setPermissionShown(true) // ensure it's set
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            !alreadyShownOnce -> {
                navController.navigate("permission") {
                    popUpTo("splash") { inclusive = true }
                }
            }

            else -> {
                // Permission not granted, and already shown once
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
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

        Text(
            text = "Developed by Arif",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp) // adjust to bring slightly above nav buttons
                .navigationBarsPadding(), // adds padding equal to nav bar height
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

    }
}
