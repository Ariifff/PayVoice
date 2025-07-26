package com.arif.payvoice.mainpage

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arif.payvoice.accessories.Routes
import com.arif.payvoice.ui.theme.SoftGray
import com.arif.payvoice.ui.theme.White


@Composable
fun SettingsScreen(navController : NavController) {
    val context = LocalContext.current
    val voiceEnabled = remember { mutableStateOf(true) }


    // 🔽 Load initial language from SharedPreferences
    val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val initialLanguage = prefs.getString("selected_language", "English") ?: "English"
    val initialGender = prefs.getString("selected_gender", "Male") ?: "Male"
    var selectedLanguage by remember { mutableStateOf(initialLanguage) }
    val selectedGender = remember { mutableStateOf(initialGender) }

    val voiceOptions = listOf("Male", "Female")
    val languageOptions = listOf("English", "Hindi")
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        Text("Voice & Accessibility", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(Modifier.padding(16.dp)) {

                Spacer(modifier = Modifier.height(12.dp))
                Text("Gender")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    voiceOptions.forEach { option ->
                        FilterChip(
                            selected = selectedGender.value == option,
                            onClick = { selectedGender.value = option
                                prefs.edit().putString("selected_gender", option).apply()},
                            label = { Text(option) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Voice Language")
                DropdownMenuField(
                    options = languageOptions,
                    selectedOption = selectedLanguage,
                    onOptionSelected = {
                        selectedLanguage = it

                        // ✅ Save to SharedPreferences
                        prefs.edit().putString("selected_language", it).apply()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        SettingsOption("App Version", subtitle = "v1.0") {}
        SettingsOption("Developed by", subtitle = "Arif") {}
        SettingsOption("FAQs") {navController.navigate("faq")}
        SettingsOption("Privacy Policy") {navController.navigate("PrivacyPolicy")}
        SettingsOption("Terms and Conditions") {navController.navigate("TermsAndConditions")}
    }
}

@Composable
fun SettingsOption(title: String, subtitle: String? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Language") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}
