package com.arif.payvoice.mainpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arif.payvoice.data.HistoryViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.arif.payvoice.ui.theme.Blue
import com.arif.payvoice.ui.theme.Purple80
import com.arif.payvoice.ui.theme.PurpleGrey80
import com.arif.payvoice.ui.theme.SoftGray
import com.arif.payvoice.ui.theme.TrustWorthyBlue
import com.arif.payvoice.ui.theme.White


@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val transactions by viewModel.transactions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White) // Your original white background
    ) {
        // Your original header - unchanged
        Text(
            "History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Blue, // Your original blue color
            modifier = Modifier.padding(16.dp)
        )
        Divider(thickness = 1.dp)

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No transactions found",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            // Your original transaction list - completely unchanged
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(transactions) { txn ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SoftGray // Your original card color
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("₹${"%.2f".format(txn.amount)} - Received",
                                style = MaterialTheme.typography.titleMedium)
                            Text(txn.appName, style = MaterialTheme.typography.bodySmall)
                            Text("${txn.date}, ${txn.time}",
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}