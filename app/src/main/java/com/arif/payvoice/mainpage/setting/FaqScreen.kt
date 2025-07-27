package com.arif.payvoice.mainpage.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arif.payvoice.ui.theme.Black
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(onBack: () -> Unit = {}) {
    val faqs = listOf(
        FAQItem(
            "How do I enable voice features?",
            "Go to Settings > Voice & Accessibility and turn on the voice toggle."
        ),
        FAQItem(
            "How can I change the language?",
            "In Settings, use the language dropdown to choose Hindi, English, or other supported languages."
        ),
        FAQItem(
            "Can I choose a male or female voice?",
            "Yes, go to Settings > Voice Options and select the preferred voice gender."
        ),
        FAQItem(
            "Does this app store my transaction data?",
            "No, we do not store your personal transaction data unless you opt-in for local history tracking."
        ),
        FAQItem(
            "Is my data safe?",
            "Yes, your data is encrypted and follows standard privacy guidelines."
        ),
        FAQItem(
            "Does PayVoice support Bluetooth audio output?",
            "Yes, the announcements will be played on your phone speaker or any connected Bluetooth device."
        ),
        FAQItem(
            "Does this app support other Indian languages?",
            "Currently we support Hindi and English. More languages like Tamil, Bengali, and Marathi are coming soon."
        )
    )

    var expandedItems by remember { mutableStateOf(setOf<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Frequently Asked Questions",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {

                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.4f),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            itemsIndexed(faqs) { index, faq ->
                val isExpanded = index in expandedItems

                FaqCard(
                    question = faq.question,
                    answer = faq.answer,
                    isExpanded = isExpanded,
                    onClick = {
                        expandedItems = if (isExpanded) expandedItems - index else expandedItems + index
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Still have questions? Contact us at support@payvoice.com",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun FaqCard(
    question: String,
    answer: String,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isExpanded) 4.dp else 2.dp,
            pressedElevation = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Black.copy(alpha = 0.8f)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + slideInVertically(animationSpec = tween(150)),
                exit = fadeOut() + slideOutVertically(animationSpec = tween(150))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray.copy(alpha = 0.9f),
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

private data class FAQItem(
    val question: String,
    val answer: String
)
