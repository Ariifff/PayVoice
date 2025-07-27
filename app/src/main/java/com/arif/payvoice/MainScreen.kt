import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arif.payvoice.PayVoiceApp
import com.arif.payvoice.mainpage.HistoryScreen
import com.arif.payvoice.mainpage.HomeScreen
import com.arif.payvoice.mainpage.ProfileScreen
import com.arif.payvoice.accessories.Routes
import com.arif.payvoice.mainpage.SettingsScreen
import com.arif.payvoice.dataclass.BottomNavItem
import com.arif.payvoice.data.HistoryViewModel
import com.arif.payvoice.data.HistoryViewModelFactory
import com.arif.payvoice.mainpage.setting.FaqScreen
import com.arif.payvoice.mainpage.setting.PrivacyPolicyScreen
import com.arif.payvoice.mainpage.setting.TermsAndConditionsScreen


val bottomNavItems = listOf(
    BottomNavItem("Home", Routes.Home, Icons.Default.Home),
    BottomNavItem("History", Routes.History, Icons.Filled.History),
    BottomNavItem("Settings", Routes.Settings, Icons.Default.Settings),
    BottomNavItem("Profile", Routes.Profile, Icons.Default.Person),
)


@Composable
fun MainScreen(
    navController: NavHostController
) {
    val bottomNavController = rememberNavController()

    val app = LocalContext.current.applicationContext as PayVoiceApp
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(app.repository)
    )

    val currentDestination = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(Routes.Home)
                                launchSingleTop = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.name) },
                        label = { Text(item.name) }

                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Home) { HomeScreen() }
            composable(Routes.History) { HistoryScreen(viewModel) }
            composable(Routes.Settings) { SettingsScreen(bottomNavController) }
            composable(Routes.Profile) { ProfileScreen(navController) }
            composable(Routes.Faq) { FaqScreen() }
            composable(Routes.PrivacyPolicy) { PrivacyPolicyScreen(onBack = { bottomNavController.popBackStack() }) }
            composable(Routes.TermsAndConditions) { TermsAndConditionsScreen(onBack = { bottomNavController.popBackStack() }) }

        }

    }

}
