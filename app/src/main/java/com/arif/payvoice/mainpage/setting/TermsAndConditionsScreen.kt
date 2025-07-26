package com.arif.payvoice.mainpage.setting

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arif.payvoice.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms & Conditions", color = Blue) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("Effective Date: July 24, 2025")

            Spacer(modifier = Modifier.height(12.dp))

            termSection("1. App Purpose", "PayVoice reads UPI transaction notifications and announces them using Text-to-Speech (TTS). It helps users, especially shopkeepers, vendors and those with visual or accessibility needs, to get spoken alerts of received payments.")

            termSection("2. User Responsibilities", "• Enable notifications for UPI apps like Google Pay, PhonePe, Paytm.\n• Select your preferred UPI app and voice in Settings.\n• You are responsible for the security and accuracy of your device data.")

            termSection("3. Permissions", "The app requires:\n• Notification Access: To detect UPI transaction messages.\n• Internet: For analytics and TTS (if cloud used).\n• Storage (optional): For offline voice support.")

            termSection("4. Data Handling", "PayVoice does not share or store sensitive data outside your device. Refer to our Privacy Policy for more details.")

            termSection("5. Prohibited Use", "• Do not use the app for fraud or unauthorized transactions.\n• Do not reverse-engineer or redistribute.\n• Do not misuse the voice features.")

            termSection("6. Limitation of Liability", "We are not responsible for missed or incorrect announcements, or any loss from relying solely on TTS. Always verify UPI info manually.")

            termSection("7. Termination", "We may terminate app access if any misuse or violation is found.")

            termSection("8. Changes to Terms", "We may update these Terms from time to time. Continued use after changes implies acceptance.")

            termSection("9. Contact", "📧 arifdevelopment1@gmail.com")


        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.Gray,
        fontWeight = FontWeight.Light
    )
}

@Composable
fun termSection(title: String, content: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Text(
        text = content,
        fontSize = 16.sp,
        lineHeight = 22.sp
    )
}
