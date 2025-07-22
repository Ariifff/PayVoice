package com.arif.payvoice

import MainScreen
import com.arif.payvoice.starter.PermissionScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arif.payvoice.accessories.Routes
import com.arif.payvoice.starter.OnboardingScreen
import com.arif.payvoice.starter.SplashScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                AppNavHost()
            }

        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash
    ) {
        composable(Routes.Splash) {
            SplashScreen(navController)
            LaunchedEffect(Unit) {
                delay(3000)
                navController.navigate(Routes.Onboarding) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
        }
        composable(Routes.Onboarding) {
            OnboardingScreen(
                onContinueClicked = {
                    navController.navigate(Routes.Permission)
                }
            )
        }
        composable(Routes.Permission) {
            PermissionScreen(
                onGrantPermission = {
                    navController.navigate(Routes.Main) {
                        popUpTo(Routes.Permission) { inclusive = true }

                    }
                }
            )
        }
        composable(Routes.Main) {
            MainScreen()
        }

    }
}
