package com.arif.payvoice.mainpage


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arif.payvoice.ui.theme.SoftGray

@Preview
@Composable
fun SettingsScreen() {
    val voiceEnabled = remember { mutableStateOf(true) }
    val selectedSpeed = remember { mutableStateOf("Normal") }
    val selectedLanguage = remember { mutableStateOf("English") }
    val voiceOptions = listOf("Slow", "Normal", "Fast")
    val languageOptions = listOf("English", "Hindi")
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(16.dp)) {

        Text("Voice & Accessibility", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = SoftGray)
        )
        {
            Column(Modifier.padding(16.dp)) {


                Spacer(modifier = Modifier.height(12.dp))
                Text("Voice Speed")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    voiceOptions.forEach { option ->
                        FilterChip(
                            selected = selectedSpeed.value == option,
                            onClick = { selectedSpeed.value = option },
                            label = { Text(option) },
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("Voice Language")
                DropdownMenuField(
                    options = languageOptions,
                    selectedOption = selectedLanguage.value,
                    onOptionSelected = { selectedLanguage.value = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Support & Feedback", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        SettingsOption("FAQs") {}
        SettingsOption("Report a Bug") {}
        SettingsOption("Send Feedback") {}
        SettingsOption("Contact Support") {}

        Spacer(modifier = Modifier.height(24.dp))
        Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        SettingsOption("App Version", subtitle = "v1.0") {}
        SettingsOption("Developed by", subtitle = "Arif") {}
        SettingsOption("Privacy Policy") {}
        SettingsOption("Terms and Conditions") {}
    }
}

@Composable
fun SettingsOption(title: String, subtitle: String? = null, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = SoftGray),
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
