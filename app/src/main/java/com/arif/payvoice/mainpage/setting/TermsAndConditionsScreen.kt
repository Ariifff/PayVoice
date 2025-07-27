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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Terms & Conditions",
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
                .background(color = White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("Effective Date: July 24, 2025")

            Spacer(modifier = Modifier.height(16.dp))

            TermSection(
                title = "1. App Purpose",
                content = "PayVoice reads UPI transaction notifications and announces them using Text-to-Speech (TTS). It helps users, especially shopkeepers, vendors and those with visual or accessibility needs, to get spoken alerts of received payments."
            )

            TermSection(
                title = "2. User Responsibilities",
                content = "• Enable notifications for UPI apps like Google Pay, PhonePe, Paytm.\n• Select your preferred UPI app and voice in Settings.\n• You are responsible for the security and accuracy of your device data."
            )

            TermSection(
                title = "3. Permissions",
                content = "The app requires:\n• Notification Access: To detect UPI transaction messages.\n• Internet: For analytics and TTS (if cloud used).\n• Storage (optional): For offline voice support."
            )

            TermSection(
                title = "4. Data Handling",
                content = "PayVoice does not share or store sensitive data outside your device. Refer to our Privacy Policy for more details."
            )

            TermSection(
                title = "5. Prohibited Use",
                content = "• Do not use the app for fraud or unauthorized transactions.\n• Do not reverse-engineer or redistribute.\n• Do not misuse the voice features."
            )

            TermSection(
                title = "6. Limitation of Liability",
                content = "We are not responsible for missed or incorrect announcements, or any loss from relying solely on TTS. Always verify UPI info manually."
            )

            TermSection(
                title = "7. Termination",
                content = "We may terminate app access if any misuse or violation is found."
            )

            TermSection(
                title = "8. Changes to Terms",
                content = "We may update these Terms from time to time. Continued use after changes implies acceptance."
            )

            TermSection(
                title = "9. Contact",
                content = "📧 arifdevelopment1@gmail.com"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.Gray,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun TermSection(title: String, content: String) {
    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            color = Color.DarkGray
        )
    }
}