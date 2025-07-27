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
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ){
        Text("History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Blue,
            modifier = Modifier.padding(16.dp)
        )
        Divider( thickness = 1.dp)

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(transactions) { txn ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SoftGray
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("₹${"%.2f".format(txn.amount)} - Received", style = MaterialTheme.typography.titleMedium)
                        Text(txn.appName, style = MaterialTheme.typography.bodySmall)
                        Text("${txn.date}, ${txn.time}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }


}
