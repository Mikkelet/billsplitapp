package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ExpenseHolder(val person: Person, protected var expense: Float) {

    var expenseState by mutableStateOf(expense)
    var nameState by mutableStateOf(person.nameState)

    open fun isChanged(): Boolean {
        return expenseState != expense || nameState != person.nameState
    }

    open fun revertChanges() {
        expenseState = expense
        nameState = person.nameState
    }

    open fun saveChanges() {
        expense = expenseState
    }

    abstract fun copy(): ExpenseHolder

    class IndividualExpenseHolder(
        person: Person,
        expense: Float,
        private var isParticipant: Boolean = true
    ) : ExpenseHolder(person, expense) {

        var isParticipantState by mutableStateOf(isParticipant)

        fun reset() = IndividualExpenseHolder(person, 0F, true)

        override fun isChanged() = super.isChanged() || isParticipantState != isParticipant

        override fun revertChanges() {
            super.revertChanges()
            isParticipantState = isParticipant
        }

        override fun toString(): String {
            return "IndividualExpenseHolder(name=$nameState, expense=$expenseState, isParticipant=$isParticipantState)"
        }

        override fun copy() = IndividualExpenseHolder(person, expense, isParticipant)

        override fun saveChanges() {
            super.saveChanges()
            isParticipant = isParticipantState
        }
    }

    class SharedExpenseHolder(expense: Float) :
        ExpenseHolder(Person("0", "Shared"), expense) {
        override fun copy() = SharedExpenseHolder(expense)
        override fun toString() = "SharedExpenseHolder(expense=$expenseState)"
    }
}