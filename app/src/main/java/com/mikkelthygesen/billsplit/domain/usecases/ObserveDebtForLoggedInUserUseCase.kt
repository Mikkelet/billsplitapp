package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.Person
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class ObserveDebtForLoggedInUserUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb,
    private val authProvider: AuthProvider
) {

    fun observe(groupId: String): Flow<List<Pair<Person, Float>>> {
        val paymentsFlow = billSplitDb.paymentsDao().getPaymentsFlow(groupId)
            .map { payments -> payments.map { dto -> Payment(dto) } }
        val expensesFlow = billSplitDb.groupExpensesDao().getGroupExpensesFlow(groupId)
            .map { expenses -> expenses.map { dto -> GroupExpense(dto) } }
        return paymentsFlow.combine(expensesFlow) { payments: List<Payment>, expenses: List<GroupExpense> ->
            val group = billSplitDb.groupsDao().getGroup(groupId).let { Group(it) }
            val calculator = DebtCalculator(group.peopleState, expenses, payments)
            calculator.calculateEffectiveDebtOfPerson(authProvider.requireLoggedInUser)
        }
    }
}