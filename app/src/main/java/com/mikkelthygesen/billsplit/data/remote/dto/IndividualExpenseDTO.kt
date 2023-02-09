package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.domain.models.IndividualExpense

@kotlinx.serialization.Serializable
data class IndividualExpenseDTO(
    val person: PersonDTO,
    val expense: Float,
    val isParticipant: Boolean
) {

    constructor(individualExpense: IndividualExpense):this(
        person = PersonDTO.fromPerson(individualExpense.person),
        expense = individualExpense.expenseState,
        isParticipant = individualExpense.isParticipantState
    )
}