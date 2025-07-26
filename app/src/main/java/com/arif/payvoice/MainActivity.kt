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
import com.arif.payvoice.starter.LoginScreen
import com.arif.payvoice.starter.SignUpScreen
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
        }

        composable(Routes.Permission) {
            PermissionScreen(
                onGrantPermission = {
                    navController.navigate(Routes.Main) {
                        popUpTo("permission") { inclusive = true }

                    }
                }
            )
        }
        composable(Routes.Login) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // do login
                },
                onGoogleClick = {
                    // google login
                },
                onSignupClick = {
                    navController.navigate("signup")
                }
            )
        }
        composable(Routes.SignUp){
            SignUpScreen(
                onSignUpClick = {
                    // do signup
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        }
        composable(Routes.Main) {
            MainScreen()
        }

    }
}
