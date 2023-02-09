package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.Person
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetDebtForLoggedInUserUseCase @Inject constructor(
    private val billSplitDb: BillSplitDb,
    private val authProvider: AuthProvider
) {

    suspend fun execute(groupId: String): List<Pair<Person, Float>> {
        val group = billSplitDb.groupsDao().getGroup(groupId)
        val people = group.people.map { Person(it) }
        val groupExpenses = billSplitDb.groupExpensesDao().getGroupExpenses(groupId)
            .map { GroupExpense(it) }
        val payments = billSplitDb.paymentsDao().getPayments(groupId)
            .map { payments -> Payment(payments) }

        val calculator = DebtCalculator(people, groupExpenses, payments)
        return calculator.calculateEffectiveDebtOfPerson(authProvider.requireLoggedInUser)
    }
}