package com.mikkelthygesen.billsplit.domain.repositories

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(
    private val billSplitDb: BillSplitDb
) {
    suspend fun clearDatabase() {
        billSplitDb.eventsDao().clearPaymentTable()
        billSplitDb.eventsDao().clearChangesTable()
        billSplitDb.eventsDao().clearExpensesTable()
        billSplitDb.groupsDao().clearTable()
        billSplitDb.friendsDao().clearTable()
        billSplitDb.servicesDao().clearTable()
    }
}