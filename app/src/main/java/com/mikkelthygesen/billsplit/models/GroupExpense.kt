package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder
import com.mikkelthygesen.billsplit.models.ExpenseHolder.SharedExpenseHolder
import com.mikkelthygesen.billsplit.models.interfaces.IShareable
import com.mikkelthygesen.billsplit.reduceOrZero
import com.mikkelthygesen.billsplit.tryCatchDefault

data class GroupExpense(
    val id: String,
    private var description: String,
    private var payee: IndividualExpenseHolder,
    val sharedExpense: SharedExpenseHolder,
    val individualExpenses: List<IndividualExpenseHolder>,
) : IShareable {
    override val timeStamp: Long = System.currentTimeMillis()

    var descriptionState by mutableStateOf(description)
    var payeeState by mutableStateOf(payee)

    private val sharedExpensePerParticipant
        get() = tryCatchDefault(0F) {
            sharedExpense.expenseState / participants
        }
    private val participants: Int
        get() = individualExpenses.count { it.isParticipantState }

    fun getTotal(): Float {
        val expenseHolders = individualExpenses.plus(sharedExpense)
        return expenseHolders
            .map { it.expenseState }
            .reduceOrZero()
    }

    fun getIndividualExpensesWithShared() = individualExpenses.map { ie ->
        ie.copy().apply {
            if (isParticipantState)
                expenseState += sharedExpensePerParticipant
        }
    }

    override fun toString(): String {
        return "GroupExpense(id=$id, description=$descriptionState, payee=$payeeState, sharedExpense=$sharedExpense, expense=$individualExpenses)"
    }

    fun isChanged(): Boolean {
        return sharedExpense.isChanged()
                || individualExpenses.any { it.isChanged() }
                || descriptionState != description
                || payeeState != payee
    }

    fun revertChanges() {
        descriptionState = description
        payeeState = payee
        sharedExpense.revertChanges()
        individualExpenses
            .plus(sharedExpense)
            .forEach { it.revertChanges() }
    }

    fun saveChanges() {
        description = descriptionState
        payee = payeeState
        individualExpenses
            .plus(sharedExpense)
            .forEach { it.saveChanges() }
    }
}