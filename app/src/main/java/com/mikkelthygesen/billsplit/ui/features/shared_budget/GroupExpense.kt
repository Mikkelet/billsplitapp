package com.mikkelthygesen.billsplit.ui.features.shared_budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder

class GroupExpense(
    val id: String,
    description: String,
    val payee: IndividualExpenseHolder,
    val sharedExpense: SharedExpenseHolder,
    val individualExpenses: List<IndividualExpenseHolder>
) {
    var description by mutableStateOf(description)

    var participants by mutableStateOf(individualExpenses.count { it.isParticipant })

    fun getTotal(): Float {
        val expenseHolders = individualExpenses.plus(sharedExpense)
        return expenseHolders
            .map { it.expense }
            .reduce { acc, fl -> acc + fl }
    }

    fun getSharedDivided() = sharedExpense.expense / participants

    fun filterPayee() = GroupExpense(
        id,
        description,
        payee,
        sharedExpense,
        individualExpenses.filter { it != payee }
    )

    fun getIndividualExpensesWithShared() = individualExpenses.map {
        it.copy().apply {
            expense += getSharedDivided()
        }
    }

    override fun toString(): String {
        return "GroupExpense(id=$id, description=$description, payee=$payee, sharedExpense=$sharedExpense, expense=$individualExpenses)"
    }
}