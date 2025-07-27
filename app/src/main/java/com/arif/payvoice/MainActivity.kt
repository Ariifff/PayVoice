package com.arif.payvoice

import MainScreen
import com.arif.payvoice.starter.PermissionScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arif.payvoice.accessories.Routes
import com.arif.payvoice.starter.ForgotPasswordScreen
import com.arif.payvoice.starter.LoginScreen
import com.arif.payvoice.starter.SignUpScreen
import com.arif.payvoice.starter.SplashScreen

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
        startDestination = "splash"
    ) {
        composable(Routes.Splash) {
            SplashScreen(navController)
        }

        composable(Routes.Permission) {
            PermissionScreen(
                onGrantPermission = {
                    navController.navigate("main") {
                        popUpTo("permission") { inclusive = true }

                    }
                }
            )
        }
        composable(Routes.Login) {
            LoginScreen(
                navController = navController,
                onGoogleClick = {
                    // google login
                },
                onSignupClick = {
                    navController.navigate("signup")
                },
                onForgotClick = {
                    navController.navigate("forgotpassword")
                }
            )
        }
        composable(Routes.SignUp) {
            SignUpScreen(
                onSignupSuccess = {
                    navController.navigate(Routes.Main) {
                        popUpTo(Routes.SignUp) { inclusive = true }
                    }
                }
            )
        }


        composable(Routes.ForgotPassword){
            ForgotPasswordScreen()
        }

        composable(Routes.Main) {
            MainScreen(
                navController = navController
            )
        }

    }
}
