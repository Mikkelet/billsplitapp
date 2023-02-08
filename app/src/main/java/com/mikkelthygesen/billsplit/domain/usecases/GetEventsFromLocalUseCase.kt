package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetEventsFromLocalUseCase @Inject constructor(
    private val database: BillSplitDb,
) {

    suspend fun execute(groupId: String): List<Event> {
        val expenses: List<Event> =
            database.groupExpensesDao().getAllForGroup(groupId).map { GroupExpense(it) }
        val payments: List<Event> =
            database.paymentsDao().getPayments(groupId).map { Payment(it) }
        val changes: List<Event> =
            database.expenseChangesDao().getExpenseChanges(groupId).map { GroupExpensesChanged(it) }
        return expenses.plus(payments).plus(changes).sortedBy { it.timeStamp }.reversed()

    }
}