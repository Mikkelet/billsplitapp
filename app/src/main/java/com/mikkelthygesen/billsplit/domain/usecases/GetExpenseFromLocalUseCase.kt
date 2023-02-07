package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.domain.models.*
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetExpenseFromLocalUseCase @Inject constructor(
    private val database: BillSplitDb,
    private val authProvider: AuthProvider
) {

    suspend fun execute(
        expenseId: String,
        groupId: String
    ): GroupExpense {
        // if expenseId is empty, load new expense
        if (expenseId.isBlank()) {
            val groupDb = database.groupsDao().getGroup(groupId = groupId)
            val group = Group(groupDb)
            return GroupExpense(
                createdBy = authProvider.requireLoggedInUser,
                payee = authProvider.requireLoggedInUser,
                individualExpenses = group.peopleState.map { IndividualExpense(it) },
            )
        }
        val groupExpenseDb = database.eventsDao().getGroupExpense(expenseId)
        return GroupExpense(groupExpenseDb)
    }
}