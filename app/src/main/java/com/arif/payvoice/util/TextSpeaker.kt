package com.arif.payvoice.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

object TextSpeaker {
    private var tts: TextToSpeech? = null

    fun speak(context: Context, message: String) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) {
                if (it == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        } else {
            tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
}