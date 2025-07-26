package com.arif.payvoice.mainpage.setting

import androidx.compose.material.icons.Icons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.arif.payvoice.ui.theme.Blue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy", color = Blue) },
                navigationIcon = {
                    IconButton(onClick = { onBack }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Privacy Policy — PayVoice",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Effective Date: July 24, 2025",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Section("1. Data Collection", """
                • We do NOT collect or store any personal data or financial information.
                • PayVoice reads UPI transaction notifications locally on your device to generate voice announcements.
                • No data is sent to any server or shared with third parties.
            """)

            Section("2. Permissions Used", """
                PayVoice requires:
                • Notification Access – to read incoming UPI transaction messages.
                • Text-to-Speech – to convert transaction text into speech.
                • Internet (optional) – for better voice quality if cloud TTS is used in future.
            """)

            Section("3. How We Use Your Data", """
                • All processing is done on your device only.
                • App settings (language, voice type, preferred UPI app) are stored locally.
            """)

            Section("4. What We Don’t Do", """
                • We do NOT collect, track, or share any of your personal or financial data.
                • We do NOT use any ad or tracking service.
                • We do NOT upload any data to a server.
            """)

            Section("5. Your Control", """
                • You can turn voice announcements ON or OFF from Home.
                • You may uninstall the app at any time to remove all local data.
            """)

            Section("6. Contact", """
                If you have questions about this policy, contact:
                📧 arifdevelopment1@gmail.com
            """)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "We may update this Privacy Policy. Changes will be reflected here.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun Section(title: String, body: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Text(
        text = body.trimIndent(),
        fontSize = 14.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
