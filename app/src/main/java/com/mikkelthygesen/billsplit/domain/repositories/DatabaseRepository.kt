package com.mikkelthygesen.billsplit.domain.repositories

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(
    private val billSplitDb: BillSplitDb
) {
    suspend fun clearDatabase() {
        billSplitDb.paymentsDao().clearTable()
        billSplitDb.groupExpensesDao().clearTable()
        billSplitDb.expenseChangesDao().clearTable()
        billSplitDb.groupsDao().clearTable()
        billSplitDb.friendsDao().clearTable()
        billSplitDb.servicesDao().clearTable()
    }
}