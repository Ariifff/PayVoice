package com.arif.payvoice.mainpage.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Privacy Policy",
                        color = Blue,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Effective Date: July 24, 2025",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            PrivacySection(
                title = "1. Data Collection",
                content = """
                    • We do NOT collect or store any personal data or financial information
                    • PayVoice reads UPI transaction notifications locally on your device
                    • No data is sent to servers or shared with third parties
                """
            )

            PrivacySection(
                title = "2. Permissions Used",
                content = """
                    PayVoice requires:
                    • Notification Access – to read UPI transaction messages
                    • Text-to-Speech – to convert transaction text into speech
                    • Internet (optional) – for better voice quality if cloud TTS is used
                """
            )

            PrivacySection(
                title = "3. Data Processing",
                content = """
                    • All processing occurs on your device only
                    • App settings are stored locally on your device
                    • Transaction data is never stored permanently
                """
            )

            PrivacySection(
                title = "4. What We Don't Do",
                content = """
                    • We never collect personal or financial data
                    • We don't use ads or tracking services
                    • We don't upload any data to external servers
                """
            )

            PrivacySection(
                title = "5. Your Control",
                content = """
                    • Toggle voice announcements ON/OFF anytime
                    • Change voice preferences in Settings
                    • Uninstall to remove all local data
                """
            )

            PrivacySection(
                title = "6. Contact Us",
                content = """
                    For questions about this policy:
                    📧 arifdevelopment1@gmail.com
                """
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "We may update this Privacy Policy. Changes will be reflected here.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                fontSize = 13.sp,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun PrivacySection(title: String, content: String) {
    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Text(
            text = content.trimIndent(),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = Color.DarkGray
        )
    }
}