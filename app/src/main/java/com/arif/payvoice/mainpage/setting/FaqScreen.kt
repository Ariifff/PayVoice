package com.arif.payvoice.mainpage.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arif.payvoice.ui.theme.Black
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.White

@Composable
fun FaqScreen() {
    val faqs = listOf(
        "How do I enable voice features?" to "Go to Settings > Voice & Accessibility and turn on the voice toggle.",
        "How can I change the language?" to "In Settings, use the language dropdown to choose Hindi, English, or other supported languages.",
        "Can I choose a male or female voice?" to "Yes, go to Settings > Voice Options and select the preferred voice gender.",
        "Does this app store my transaction data?" to "No, we do not store your personal transaction data unless you opt-in for local history tracking.",
        "Is my data safe?" to "Yes, your data is encrypted and follows standard privacy guidelines.",
        "Does PayVoice support Bluetooth audio output?" to "Yes, the announcements will be played on your phone speaker or any connected Bluetooth device.",
        "Does this app support other Indian languages?" to "Currently we support Hindi and English. More languages like Tamil, Bengali, and Marathi are coming soon."
    )

    var expandedItems by remember { mutableStateOf(setOf<Int>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        item {
            Text(
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Blue
            )
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))
        }

        itemsIndexed(faqs) { index, faq ->
            val (question, answer) = faq
            val isExpanded = index in expandedItems

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        expandedItems = if (isExpanded) expandedItems - index else expandedItems + index
                    },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
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
                            tint = Black
                        )
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = answer,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
