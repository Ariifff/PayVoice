package com.arif.payvoice.data

class TransactionRepository(private val dao: TransactionDao) {
    val allTransactions = dao.getAllTransactions()
    suspend fun insert(transaction: Transaction) = dao.insert(transaction)
    suspend fun clearAll() = dao.clearAll()
}



