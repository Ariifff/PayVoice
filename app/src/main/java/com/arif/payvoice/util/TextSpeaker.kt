package com.arif.payvoice.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

object TextSpeaker {
    private var tts: TextToSpeech? = null
    private const val TAG = "TextSpeaker"

    fun speak(context: Context, message: String) {
        val selectedVoiceName = context.getSelectedVoiceName()  // Get voice from prefs
        val selectedToggle = context.getToggleOn()    //Get toggle state from prefs

        if (!selectedToggle) {
            Log.d(TAG, "Voice toggle is OFF. Skipping speech.")
            return
        }


        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { result ->
                val locale = if (selectedVoiceName.startsWith("hi")) Locale("hi", "IN") else Locale("en", "IN")
                val langResult = tts?.setLanguage(locale)
                Log.d(TAG, "TTS initialized with locale: $locale, result: $langResult")

                if (result == TextToSpeech.SUCCESS) {
                    val voices = tts?.voices
                    val filteredVoices = voices?.filter {
                        it.locale == Locale("en", "IN") || it.locale == Locale("hi", "IN")
                    }?.filter {
                        it.name == "en-in-x-end-local" || it.name == "en-in-x-ena-local" ||
                                it.name == "hi-in-x-hie-local" || it.name == "hi-in-x-hia-local"
                    }

                    filteredVoices?.forEach {
                        Log.d(TAG, "Indian Voice Available: ${it.name} (${it.locale.displayLanguage})")
                    }

                    val selectedVoice = voices?.find { it.name == selectedVoiceName }
                    if (selectedVoice != null) {
                        tts?.voice = selectedVoice
                        Log.d(TAG, "Selected voice: ${selectedVoice.name}")
                    } else {
                        Log.w(TAG, "Preferred voice not found: $selectedVoiceName")
                    }

                    tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "PayVoiceTTS")
                }
            }
        } else {
            // 🛠 FIX: Set voice again before speaking
            val voices = tts?.voices
            val selectedVoice = voices?.find { it.name == selectedVoiceName }
            selectedVoice?.let {
                tts?.voice = it
                Log.d(TAG, "Reused TTS: voice set to ${it.name}")
            } ?: Log.w(TAG, "Reused TTS: preferred voice not found: $selectedVoiceName")


            tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "PayVoiceTTS")
        }
    }
}
