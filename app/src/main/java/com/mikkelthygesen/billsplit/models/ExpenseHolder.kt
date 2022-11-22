package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.R


data class IndividualExpense(
    val person: Person,
    private var expense: Float,
    private var isParticipant: Boolean = true
) {
    var expenseState by mutableStateOf(expense)
    var isParticipantState by mutableStateOf(isParticipant)

    fun reset() = IndividualExpense(person, 0F, true)

    fun isChanged() = isParticipantState != isParticipant || expense != expenseState

    fun revertChanges() {
        isParticipantState = isParticipant
        expenseState = expense
    }

    fun saveChanges() {
        isParticipant = isParticipantState
        expense = expenseState
    }

    override fun toString(): String {
        return "ExpenseHolder(name=$person, expense=$expenseState, isParticipant=$isParticipantState)"
    }

    fun isShared() = person.pId == getSharedExpenseHolder().person.pId

    companion object {
        private const val SHARED_ID = "-1"
        fun getSharedExpenseHolder(expense: Float = 0F) = IndividualExpense(
            Person(SHARED_ID, "Shared", R.drawable.ic_baseline_groups_24),
            expense
        )
    }
}