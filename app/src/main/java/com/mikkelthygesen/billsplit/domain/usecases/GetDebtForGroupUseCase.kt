package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.DebtCalculator
import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.*
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetDebtForGroupUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb,
) {

    suspend fun execute(
        groupId: String,
        event: Event,
        isDelete: Boolean = false
    ): List<Pair<String, Float>> {
        val group = billSplitDb.groupsDao().getGroup(groupId)
        val people = group.people.map { Person(it) }
        val groupExpenses = billSplitDb.groupExpensesDao().getGroupExpenses(groupId)
            .map { GroupExpense(it) }
            .let { expenses ->
                when (event) {
                    is GroupExpense -> if (isDelete) expenses.minus(event) else expenses.plus(event)
                    is GroupExpensesChanged -> expenses.replace(event.groupExpenseEdited)
                    is DeleteExpense -> expenses.minus(event.expense)
                    else -> expenses
                }
            }

        val payments = billSplitDb.paymentsDao().getPayments(groupId)
            .map { payments -> Payment(payments) }
            .let { if (event is Payment) it.plus(event) else it }

        val calculator = DebtCalculator(people, groupExpenses, payments)
        return calculator.calculateEffectiveDebtForGroup()
    }

    private fun List<GroupExpense>.replace(groupExpense: GroupExpense): List<GroupExpense> =
        filter { expense -> expense.id != groupExpense.id }
            .plus(groupExpense)
}