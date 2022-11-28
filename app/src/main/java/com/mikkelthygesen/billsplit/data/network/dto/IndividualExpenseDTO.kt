package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.IndividualExpense
import com.mikkelthygesen.billsplit.models.Person

@kotlinx.serialization.Serializable
data class IndividualExpenseDTO(
    val person: String,
    val expense: Float,
    val isParticipant: Boolean
) {

    fun toIndividualExpense() = IndividualExpense(
        expense = expense,
        person = Person(uid = person),
        isParticipant = isParticipant
    )

    companion object {
        fun fromIndividualExpense(individualExpense: IndividualExpense) = IndividualExpenseDTO(
            person = individualExpense.person.uid,
            expense = individualExpense.expenseState,
            isParticipant = individualExpense.isParticipantState
        )
    }
}