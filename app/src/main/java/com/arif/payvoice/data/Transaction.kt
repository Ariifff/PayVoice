package com.arif.payvoice.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val appName: String,
    val amount: Double,
    val description: String,
    val date: String,
    val time: String
)
