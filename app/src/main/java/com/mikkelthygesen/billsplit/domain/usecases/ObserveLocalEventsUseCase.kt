package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.GroupExpensesChanged
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class ObserveLocalEventsUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb
) {

    fun observe(groupId: String): Flow<List<Event>> {
        val expenseChangesFlow: Flow<List<GroupExpensesChanged>> =
            billSplitDb.expenseChangesDao().getExpenseChangesFlow(groupId)
                .map { expenseChanges -> expenseChanges.map { GroupExpensesChanged(it) } }
        val groupExpensesFlow: Flow<List<GroupExpense>> =
            billSplitDb.groupExpensesDao().getGroupExpensesFlow(groupId)
                .map { groupExpenses -> groupExpenses.map { GroupExpense(it) } }
        val paymentsFlow: Flow<List<Payment>> =
            billSplitDb.paymentsDao().getPaymentsFlow(groupId)
                .map { payments -> payments.map { Payment(it) } }

        return expenseChangesFlow.combine(
            flow = groupExpensesFlow,
            transform = { expenseChanges, groupExpenses -> groupExpenses.plus(expenseChanges) }
        ).combine(
            flow = paymentsFlow,
            transform = { expensesAndChanges, payments -> expensesAndChanges.plus(payments) }
        )

    }
}