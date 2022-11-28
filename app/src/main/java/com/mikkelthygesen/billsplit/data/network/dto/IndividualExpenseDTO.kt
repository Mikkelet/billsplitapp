package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.IndividualExpense

@kotlinx.serialization.Serializable
data class IndividualExpenseDTO(
    val person: String,
    val expense: Float,
    val isParticipant: Boolean
) {
    companion object {
        fun fromIndividualExpense(individualExpense: IndividualExpense) = IndividualExpenseDTO(
            person = individualExpense.person.uid,
            expense = individualExpense.expenseState,
            isParticipant = individualExpense.isParticipantState
        )
    }
}