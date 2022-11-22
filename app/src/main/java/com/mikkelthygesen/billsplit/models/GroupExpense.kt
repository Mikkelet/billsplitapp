package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.models.interfaces.IShareable
import com.mikkelthygesen.billsplit.reduceOrZero
import com.mikkelthygesen.billsplit.tryCatchDefault

data class GroupExpense(
    val id: String,
    val createdBy: Person,
    private var description: String,
    private var payee: Person,
    private var sharedExpense: Float,
    val individualExpenses: List<IndividualExpense>,
    override val timeStamp: Long = System.currentTimeMillis()
) : IShareable {

    var descriptionState by mutableStateOf(description)
    var payeeState by mutableStateOf(payee)
    var sharedExpenseState by mutableStateOf(sharedExpense)

    private val sharedExpensePerParticipant
        get() = tryCatchDefault(0F) {
            sharedExpenseState / participants
        }
    private val participants: Int
        get() = individualExpenses.count { it.isParticipantState }

    fun getTotal(): Float {
        return individualExpenses.map { it.expenseState }.reduceOrZero() + sharedExpenseState
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
        return sharedExpense != sharedExpenseState
                || individualExpenses.any { it.isChanged() }
                || descriptionState != description
                || payeeState != payee
    }

    fun revertChanges() {
        descriptionState = description
        payeeState = payee
        sharedExpenseState = sharedExpense
        individualExpenses.forEach { it.revertChanges() }
    }

    fun saveChanges() {
        description = descriptionState
        payee = payeeState
        sharedExpense = sharedExpenseState
        individualExpenses.forEach { it.saveChanges() }
    }
}