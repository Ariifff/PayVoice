package com.arif.payvoice.mainpage

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arif.payvoice.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var isVoiceOn by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("payvoice_prefs", Context.MODE_PRIVATE)

    val availableApps = listOf("GPay", "Paytm", "PhonePe")
    var expanded by remember { mutableStateOf(false) }
    var selectedApp by remember {
        mutableStateOf(sharedPreferences.getString("upiApp", "GPay") ?: "GPay")
    }

    fun saveAppChoice(app: String) {
        sharedPreferences.edit().putString("upiApp", app).apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to \n PayVoice",
            style = MaterialTheme.typography.headlineSmall.copy(
                lineHeight = 50.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Blue
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Keep Voice On",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp)
            )
            Switch(
                checked = isVoiceOn,
                onCheckedChange = { isVoiceOn = it }
            )
        }

        Text(
            text = if (isVoiceOn)
                "Voice feedback is enabled. Transactions will be announced."
            else
                "Voice feedback is off. You can turn it on anytime.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select UPI App",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(Color.LightGray.copy(alpha = 0.2f))
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedApp,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableApps.forEach { app ->
                    DropdownMenuItem(
                        text = { Text(app) },
                        onClick = {
                            selectedApp = app
                            expanded = false
                            saveAppChoice(app)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selected App: $selectedApp",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
        )
    }
}
