package com.arif.payvoice.data

class TransactionRepository(private val dao: TransactionDao) {
    val allTransactions = dao.getAllTransactions()
}



