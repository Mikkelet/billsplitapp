package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ExpenseHolder(val person: Person, private var expense: Float) {
    var expenseState by mutableStateOf(expense)
    var nameState by mutableStateOf(person.nameState)

    open fun isChanged(): Boolean {
        return expenseState != expense || nameState != person.nameState
    }
    open fun revertChanges() {
        expenseState = expense
        nameState = person.nameState
    }

    open fun saveChanges(){
        expense = expenseState
    }

    class IndividualExpenseHolder(
        person: Person,
        expense: Float,
        private var isParticipant: Boolean = true
    ) :
        ExpenseHolder(person, expense) {
        var isParticipantState by mutableStateOf(isParticipant)

        override fun isChanged(): Boolean {
            return super.isChanged() || isParticipantState != isParticipant
        }

        override fun revertChanges() {
            super.revertChanges()
            isParticipantState = isParticipant
        }

        override fun toString(): String {
            return "IndividualExpenseHolder(name=$nameState, expense=$expenseState, isParticipant=$isParticipantState)"
        }

        fun copy() = IndividualExpenseHolder(
            person,
            expenseState,
            isParticipantState
        )

        override fun saveChanges() {
            super.saveChanges()
            isParticipant = isParticipantState
        }
    }

    class SharedExpenseHolder(expense: Float) : ExpenseHolder(Person("0", "Shared"), expense) {
        override fun toString(): String {
            return "SharedExpenseHolder(expense=$expenseState)"
        }
    }

}
