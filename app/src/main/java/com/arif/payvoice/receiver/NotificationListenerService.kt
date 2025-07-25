package com.arif.payvoice.receiver

import android.app.Notification
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.arif.payvoice.util.TextSpeaker
import com.arif.payvoice.util.getSelectedVoiceName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NotificationListener : NotificationListenerService() {
    private val paymentApps = mapOf(
        "net.one97.paytm" to "Paytm",
        "com.google.android.apps.nbu.paisa.user" to "Google Pay",
        "com.phonepe.app" to "PhonePe"
    )

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            val pkg = sbn.packageName
            val appName = paymentApps[pkg] ?: return

            val extras = sbn.notification.extras
            val content = buildString {
                extras.keySet().forEach { key ->
                    when (val value = extras.get(key)) {
                        is CharSequence -> append(value).append(" ")
                        is String -> append(value).append(" ")
                    }
                }
            }

            if (content.contains("Received", ignoreCase = true) ||
                content.contains("Credited", ignoreCase = true) ||
                content.contains("paid you", ignoreCase = true)) {

                extractAmount(content)?.let { amount ->
                    Log.d("PayVoice", "DETECTED: ₹$amount from $appName")
                    CoroutineScope(Dispatchers.Main).launch {
                        val prefs = applicationContext.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                        val selectedLanguage = prefs.getString("selected_language", "English")
                        val voiceName = applicationContext.getSelectedVoiceName()

                        val message = when (selectedLanguage) {
                            "Hindi" -> {
                                if (amount % 1.0 == 0.0)
                                    "$appName पर ${amount.toInt()} रुपए प्राप्त हुए"
                                else
                                    "$appName पर ${"%.2f".format(amount)} रुपए प्राप्त हुए"
                            }
                            else -> {
                                val amountStr = if (amount % 1.0 == 0.0) {
                                    if (amount.toInt() == 1) "1 rupee" else "${amount.toInt()} rupees"
                                } else {
                                    "%.2f rupees".format(amount)
                                }
                                "Received $appName payment of $amountStr"
                            }
                        }



                            TextSpeaker.speak(applicationContext, message)

                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PayVoice", "Error processing notification", e)
        }
    }

    private fun extractAmount(text: String): Double? {
        return Regex("""(?:₹|Rs\.?|INR)\D*(\d[\d,]*(?:\.\d{1,2})?)""")
            .find(text)
            ?.groupValues
            ?.get(1)
            ?.replace(",", "")
            ?.toDoubleOrNull()
    }
}
