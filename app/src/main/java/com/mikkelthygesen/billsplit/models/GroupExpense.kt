package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder
import com.mikkelthygesen.billsplit.reduceOrZero

class GroupExpense(
    val id: String,
    description: String,
    val payee: IndividualExpenseHolder,
    val sharedExpense: SharedExpenseHolder,
    val individualExpenses: List<IndividualExpenseHolder>
) {
    var description by mutableStateOf(description)

    private val sharedExpensePerParticipant
        get() = sharedExpense.expense / participants
    private val participants: Int
        get() = individualExpenses.count { it.isParticipant }

    fun getTotal(): Float {
        val expenseHolders = individualExpenses.plus(sharedExpense)
        return expenseHolders
            .map { it.expense }
            .reduceOrZero()
    }

    fun getIndividualExpensesWithShared() = individualExpenses.map { ie ->
        ie.copy().apply {
            if (isParticipant)
                expense += sharedExpensePerParticipant
        }
    }

    override fun toString(): String {
        return "GroupExpense(id=$id, description=$description, payee=$payee, sharedExpense=$sharedExpense, expense=$individualExpenses)"
    }
}