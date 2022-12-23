package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.IndividualExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb


data class IndividualExpense(
    val person: Person,
    private var expense: Float,
    private var isParticipant: Boolean = true
) {
    var expenseState by mutableStateOf(expense)
    var isParticipantState by mutableStateOf(isParticipant)

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

    fun isShared() = person.uid == getSharedExpenseHolder().person.uid

    fun toDb() = IndividualExpenseDb(
        person = PersonDb(person.uid, person.nameState, person.pfpUrlState),
        expense = expense,
        isParticipant = isParticipant
    )

    companion object {
        private const val SHARED_ID = "-1"
        fun getSharedExpenseHolder(expense: Float = 0F) = IndividualExpense(
            Person(SHARED_ID, "Shared"),
            expense
        )
    }
}