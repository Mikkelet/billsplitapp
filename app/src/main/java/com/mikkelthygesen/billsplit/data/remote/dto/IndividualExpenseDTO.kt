package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.data.local.database.model.embedded.IndividualExpenseDb
import com.mikkelthygesen.billsplit.domain.models.IndividualExpense

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

    fun toDb() = IndividualExpenseDb(
        person.toDB(),
        expense,
        isParticipant
    )

    companion object {
        fun fromIndividualExpense(individualExpense: IndividualExpense) = IndividualExpenseDTO(
            person = PersonDTO.fromPerson(individualExpense.person),
            expense = individualExpense.expenseState,
            isParticipant = individualExpense.isParticipantState
        )
    }
}