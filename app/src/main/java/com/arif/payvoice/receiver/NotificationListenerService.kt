package com.arif.payvoice.receiver


import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.arif.payvoice.util.TextSpeaker
import com.arif.payvoice.util.getSelectedAppName
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

            val selectedApp = applicationContext.getSelectedAppName()

            // Skip notification if app doesn't match selected one
            if (appName != selectedApp) return


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

                        CoroutineScope(Dispatchers.IO).launch {
                            val now = Calendar.getInstance()
                            val date = "%02d %s %04d".format(
                                now.get(Calendar.DAY_OF_MONTH),
                                now.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH),
                                now.get(Calendar.YEAR)
                            )
                            val time = "%02d:%02d %s".format(
                                now.get(Calendar.HOUR) + if (now.get(Calendar.HOUR) == 0) 12 else 0,
                                now.get(Calendar.MINUTE),
                                if (now.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                            )

                            val transaction = com.arif.payvoice.data.Transaction(
                                appName = appName,
                                amount = amount,
                                description = content.trim(),
                                date = date,
                                time = time
                            )

                            com.arif.payvoice.data.TransactionDatabase.getInstance(applicationContext, CoroutineScope(Dispatchers.IO))
                                .transactionDao()
                                .insert(transaction)
                        }


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
