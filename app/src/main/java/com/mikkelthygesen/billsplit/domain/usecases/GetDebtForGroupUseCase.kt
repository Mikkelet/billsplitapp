package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetDebtForGroupUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb,
) {

    suspend fun execute(groupId: String, event: Event): List<Pair<String, Float>> {
        val group = billSplitDb.groupsDao().getGroup(groupId)
        val people = group.people.map { Person(it) }
        val groupExpenses = billSplitDb.groupExpensesDao().getGroupExpenses(groupId)
            .map { GroupExpense(it) }
            .let { if (event is GroupExpense) it.plus(event) else it }
        val payments = billSplitDb.paymentsDao().getPayments(groupId)
            .map { payments -> Payment(payments) }
            .let { if (event is Payment) it.plus(event) else it }

        val calculator = DebtCalculator(people, groupExpenses, payments)
        return calculator.calculateEffectiveDebtForGroup()
    }
}