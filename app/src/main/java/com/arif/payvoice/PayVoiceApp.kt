package com.arif.payvoice

import android.app.Application
import com.arif.payvoice.data.TransactionDatabase
import com.arif.payvoice.data.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PayVoiceApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { TransactionDatabase.getInstance(this, applicationScope) }
    val repository by lazy { TransactionRepository(database.transactionDao()) }
}