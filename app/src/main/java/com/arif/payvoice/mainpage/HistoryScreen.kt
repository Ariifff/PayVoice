package com.arif.payvoice.mainpage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // ✅ Important import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arif.payvoice.data.Transaction

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val transactions by viewModel.transactions.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(transactions, key = { it.id }) { txn ->
            TransactionCard(txn)
        }
    }
}

@Composable
fun TransactionCard(txn: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("₹${txn.amount}", style = MaterialTheme.typography.titleLarge)
            Text(txn.description, style = MaterialTheme.typography.bodyMedium)
            Text(txn.date, style = MaterialTheme.typography.labelSmall)
        }
    }
}
