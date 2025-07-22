package com.arif.payvoice.receiver

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.arif.payvoice.data.Transaction
import com.arif.payvoice.data.TransactionDatabase
import com.arif.payvoice.util.TextSpeaker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("PayVoice", "Notification from: ${sbn.packageName}")

        val pkg = sbn.packageName
        val extras = sbn.notification.extras
        Log.d("PayVoice", "Notification from: ${sbn.packageName}")

        for (key in extras.keySet()) {
            Log.d("PayVoice", "Key: $key -> ${extras.get(key)}")
        }

        val ticker = sbn.notification.tickerText?.toString() ?: ""
        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""

        // ✅ Fallback for text: try EXTRA_TEXT, then EXTRA_BIG_TEXT, then EXTRA_SUB_TEXT
        val text = extras.getString(Notification.EXTRA_TEXT)
            ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
            ?: extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString()
            ?: ""

        Log.d("PayVoice", "Title: $title")
        Log.d("PayVoice", "Text: $text")
        Log.d("PayVoice", "Ticker: $ticker")

        val content = "$title $text $ticker"

        val appName = when {
            pkg.contains("gpay", ignoreCase = true) -> "GPay"
            pkg.contains("paytm", ignoreCase = true) -> "Paytm"
            pkg.contains("phonepe", ignoreCase = true) -> "PhonePe"
            else -> null
        }

        val isRelevant = content.contains("Received", ignoreCase = true) ||
                content.contains("paid you", ignoreCase = true) ||
                content.contains("sent you", ignoreCase = true) ||
                content.contains("Credited", ignoreCase = true) ||
                content.contains("from", ignoreCase = true)

        if (appName != null && isRelevant) {
            val amount = extractAmount(content)

            if (amount != null) {
                val txn = Transaction(
                    amount = amount,
                    description = "Received via $appName",
                    date = Date().toString()
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val db = TransactionDatabase.getDatabase(applicationContext, this)
                    db.transactionDao().insert(txn)

                    withContext(Dispatchers.Main) {
                        TextSpeaker.speak(applicationContext, "Received ₹$amount on $appName")
                    }
                }
            }
        }
    }


    private fun extractAmount(text: String): Double? {
        val regex = Regex("""(?:₹|Rs\.?|INR)\s?([0-9]+(?:\.[0-9]+)?)""", RegexOption.IGNORE_CASE)
        return regex.find(text)?.groupValues?.get(1)?.toDoubleOrNull()
    }
}
