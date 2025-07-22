package com.arif.payvoice.accessories

import android.content.Context

object PreferenceHelper {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_ONBOARDING_DONE = "onboarding_done"

    fun setOnboardingDone(context: Context, done: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, done).apply()
    }

    fun isOnboardingDone(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false)
    }
}
