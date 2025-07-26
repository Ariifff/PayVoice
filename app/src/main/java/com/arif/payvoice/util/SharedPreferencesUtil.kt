package com.arif.payvoice.util

import android.content.Context

fun Context.getSelectedVoiceName(): String {
    val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val language = prefs.getString("selected_language", "English")
    val gender = prefs.getString("selected_gender", "Male")


    return when {
        language == "Hindi" && gender == "Male" -> "hi-in-x-hie-local"
        language == "Hindi" && gender == "Female" -> "hi-in-x-hia-local"
        language == "English" && gender == "Male" -> "en-in-x-ene-local"
        language == "English" && gender == "Female" -> "en-in-x-ena-local"
        else -> "en-in-x-end-local" // fallback
    }
}
fun Context.getToggleOn(): Boolean{
    val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val isVoiceOn = prefs.getBoolean("voice_on", true)
    return isVoiceOn
}

fun Context.getSelectedAppName(): String {
    val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
    return prefs.getString("upiApp", "Google Pay") ?: "Google Pay"
}

// ✅ Save permission_shown = true
fun Context.setPermissionShown(shown: Boolean) {
    val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("permission_shown", shown).apply()
}

// ✅ Get permission_shown value
fun Context.isPermissionShown(): Boolean {
    val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
    return prefs.getBoolean("permission_shown", false)
}


