package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.models.IndividualExpense

@kotlinx.serialization.Serializable
data class IndividualExpenseDTO(
    val person: PersonDTO,
    val expense: Float,
    val isParticipant: Boolean
) {

    fun toIndividualExpense() = IndividualExpense(
        expense = expense,
        person = person.toPerson(),
        isParticipant = isParticipant
    )

    companion object {
        fun fromIndividualExpense(individualExpense: IndividualExpense) = IndividualExpenseDTO(
            person = PersonDTO.fromPerson(individualExpense.person),
            expense = individualExpense.expenseState,
            isParticipant = individualExpense.isParticipantState
        )
    }
}