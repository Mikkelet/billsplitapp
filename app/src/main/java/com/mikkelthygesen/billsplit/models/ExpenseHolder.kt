package com.mikkelthygesen.billsplit.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class ExpenseHolder(val person: Person, expense: Float) {
    var expense by mutableStateOf(expense)
    var name by mutableStateOf(person.name)

    class IndividualExpenseHolder(person: Person, expense: Float, isParticipant: Boolean = true) :
        ExpenseHolder(person, expense) {
        var isParticipant by mutableStateOf(isParticipant)
        override fun toString(): String {
            return "IndividualExpenseHolder(name=$name, expense=$expense, isParticipant=$isParticipant)"
        }

        fun copy() = IndividualExpenseHolder(
            person,
            expense,
            isParticipant
        )
    }

    class SharedExpenseHolder(expense: Float) : ExpenseHolder(Person("0", "Shared"), expense) {
        override fun toString(): String {
            return "SharedExpenseHolder(expense=$expense)"
        }
    }


}
