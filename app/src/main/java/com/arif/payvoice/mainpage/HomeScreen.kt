package com.arif.payvoice.mainpage

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arif.payvoice.R
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.White


@Composable
fun HomeScreen() {
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Handle back button press
    BackHandler(enabled = true) {
        showExitDialog = true
    }

    HomeContent(
        voiceToggle = rememberVoiceToggleState(),
        upiAppSelection = rememberUpiAppSelectionState()
    )

    // Exit Confirmation Dialog
    if (showExitDialog) {
        ExitConfirmationDialog(
            onConfirm = {
                // Close the app
                (context as Activity).finish()
            },
            onDismiss = { showExitDialog = false }
        )
    }
}

@Composable
private fun HomeContent(
    voiceToggle: VoiceToggleState,
    upiAppSelection: UpiAppSelectionState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHeader()
        VoiceToggleSection(voiceToggle)
        UpiAppSelectionSection(upiAppSelection)
    }
}

@Composable
private fun AppHeader() {
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
}

@Composable
private fun VoiceToggleSection(state: VoiceToggleState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                checked = state.isVoiceOn,
                onCheckedChange = { state.onToggle(it) }
            )
        }

        Text(
            text = if (state.isVoiceOn)
                "Voice feedback is on. Transactions will be announced."
            else
                "Voice feedback is off. You can turn it on anytime.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun UpiAppSelectionSection(state: UpiAppSelectionState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Select UPI App",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
        )

        UpiAppDropdown(
            selectedApp = state.selectedApp,
            availableApps = state.availableApps,
            onAppSelected = state.onAppSelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selected App: ${state.selectedApp}",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
        )

        val NastaliqFont = FontFamily(
            Font(R.font.noto_nastaliq_urdu, FontWeight.Normal)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "جو ابر یہاں سے اُٹھے گا",
                style = TextStyle(
                    fontFamily = NastaliqFont,
                    textDirection = TextDirection.Rtl,
                    fontSize = 18.sp,
                    letterSpacing = 0.3.sp
                ),
                textAlign = TextAlign.Center // Center each line individually
            )

            Text(
                text = "وہ سارے جہاں پر برسے گا",
                style = TextStyle(
                    fontFamily = NastaliqFont,
                    textDirection = TextDirection.Rtl,
                    fontSize = 18.sp,
                    letterSpacing = 0.3.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun UpiAppDropdown(
    selectedApp: String,
    availableApps: List<String>,
    onAppSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
            modifier = Modifier.background(White),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableApps.forEach { app ->
                DropdownMenuItem(
                    text = { Text(app) },
                    onClick = {
                        onAppSelected(app)
                        expanded = false
                    }
                )
            }
        }
    }
}


// State holders
class VoiceToggleState(
    val isVoiceOn: Boolean,
    val onToggle: (Boolean) -> Unit
)

class UpiAppSelectionState(
    val selectedApp: String,
    val availableApps: List<String>,
    val onAppSelected: (String) -> Unit
)

@Composable
private fun rememberVoiceToggleState(): VoiceToggleState {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("Settings", Context.MODE_PRIVATE) }

    var isVoiceOn by remember {
        mutableStateOf(prefs.getBoolean("voice_on", true))
    }

    return remember(isVoiceOn) {
        VoiceToggleState(
            isVoiceOn = isVoiceOn,
            onToggle = { newValue ->
                isVoiceOn = newValue
                prefs.edit().putBoolean("voice_on", newValue).apply()
            }
        )
    }
}

@Composable
private fun rememberUpiAppSelectionState(): UpiAppSelectionState {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("Settings", Context.MODE_PRIVATE) }
    val availableApps = remember { listOf("Google Pay", "Paytm", "PhonePe") }

    var selectedApp by remember {
        mutableStateOf(prefs.getString("upiApp", availableApps.first()) ?: availableApps.first())
    }

    return remember(selectedApp) {
        UpiAppSelectionState(
            selectedApp = selectedApp,
            availableApps = availableApps,
            onAppSelected = { app ->
                selectedApp = app
                prefs.edit().putString("upiApp", app).apply()
            }
        )
    }
}

@Composable
private fun ExitConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Exit App", fontWeight = FontWeight.Bold) },
        text = { Text("Are you sure you want to exit PayVoice?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}