package com.arif.payvoice.receiver

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.arif.payvoice.util.TextSpeaker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NotificationListener : NotificationListenerService() {
    private val paymentApps = mapOf(
        "net.one97.paytm" to "Paytm",
        "com.google.android.apps.nbu.paisa.user" to "GPay",
        "com.phonepe.app" to "PhonePe"
    )

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            val pkg = sbn.packageName
            val appName = paymentApps[pkg] ?: return

            val extras = sbn.notification.extras
            // Get ALL possible text fields from notification
            val content = buildString {
                extras.keySet().forEach { key ->
                    when (val value = extras.get(key)) {
                        is CharSequence -> append(value).append(" ")
                        is String -> append(value).append(" ")
                    }
                }
            }

            if (content.contains("Received", ignoreCase = true) ||
                content.contains("Credited", ignoreCase = true)) {

                extractAmount(content)?.let { amount ->
                    Log.d("PayVoice", "DETECTED: ₹$amount from $appName")
                    CoroutineScope(Dispatchers.Main).launch {
                        TextSpeaker.speak(
                            applicationContext,
                            "Received ₹${"%.2f".format(amount)} via $appName"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PayVoice", "Error processing notification", e)
        }
    }

    // Improved amount extraction
    private fun extractAmount(text: String): Double? {
        return Regex("""(?:₹|Rs\.?|INR)\D*(\d[\d,]*(?:\.\d{1,2})?)""")
            .find(text)
            ?.groupValues
            ?.get(1)
            ?.replace(",", "")
            ?.toDoubleOrNull()
    }
}