package com.mikkelthygesen.billsplit.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.IndividualExpenseDb
import com.mikkelthygesen.billsplit.data.remote.dto.IndividualExpenseDTO


data class IndividualExpense(
    val person: Person,
    private var expense: Float,
    private var isParticipant: Boolean = true
) {
    constructor(person: Person) : this(
        person = person,
        expense = 0f,
        isParticipant = true
    )

    constructor(individualExpenseDTO: IndividualExpenseDTO) : this(
        expense = individualExpenseDTO.expense,
        person = Person(individualExpenseDTO.person),
        isParticipant = individualExpenseDTO.isParticipant
    )

    constructor(individualExpense: IndividualExpenseDb) : this(
        person = Person(individualExpense.person),
        expense = individualExpense.expense,
        isParticipant = individualExpense.isParticipant
    )


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

    companion object {
        private const val SHARED_ID = "-1"
        fun getSharedExpenseHolder(expense: Float = 0F) = IndividualExpense(
            Person(SHARED_ID, "Shared"),
            expense
        )
    }
}