package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.models.interfaces.Event
import com.mikkelthygesen.billsplit.reduceOrZero
import com.mikkelthygesen.billsplit.tryCatchDefault

data class GroupExpense(
    val id: String,
    override val createdBy: Person,
    private var description: String,
    private var payee: Person,
    private var sharedExpense: Float,
    val individualExpenses: List<IndividualExpense>,
    override val timeStamp: Long = System.currentTimeMillis()
) : Event {

    var descriptionState by mutableStateOf(description)
    var payeeState by mutableStateOf(payee)
    var sharedExpenseState by mutableStateOf(sharedExpense)

    val total:Float
        get() = individualExpenses.map { it.expenseState }.reduceOrZero() + sharedExpenseState
    private val sharedExpensePerParticipant
        get() = tryCatchDefault(0F) { sharedExpenseState / participants }
    private val participants: Int
        get() = individualExpenses.count { it.isParticipantState }

    fun getParticipants() = individualExpenses.filter { it.isParticipantState }.map { it.person }

    fun getIndividualExpensesWithShared() = individualExpenses.map { ie ->
        ie.copy().apply {
            if (isParticipantState)
                expenseState += sharedExpensePerParticipant
            saveChanges()
        }
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