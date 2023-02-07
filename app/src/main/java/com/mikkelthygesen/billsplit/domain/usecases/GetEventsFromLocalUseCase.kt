package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetEventsFromLocalUseCase @Inject constructor(
    private val database: BillSplitDb,
) {

    suspend fun execute(groupId: String): List<Event> {
        val expenses: List<Event> =
            database.eventsDao().getGroupExpenses(groupId).map { it.toGroupExpense() }
        val payments: List<Event> =
            database.eventsDao().getPayments(groupId).map { it.toPayment() }
        val changes: List<Event> =
            database.eventsDao().getExpenseChanges(groupId).map { it.toExpenseChange() }
        return expenses.plus(payments).plus(changes).sortedBy { it.timeStamp }.reversed()

    }
}