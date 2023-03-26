package com.mikkelthygesen.billsplit.data.local.database.model.embedded

import com.mikkelthygesen.billsplit.data.remote.dto.IndividualExpenseDTO
import com.mikkelthygesen.billsplit.domain.models.IndividualExpense

@kotlinx.serialization.Serializable
data class IndividualExpenseDb(
    val person: PersonDb,
    val expense: Float,
    val isParticipant: Boolean
) {

    constructor(individualExpenseDTO: IndividualExpenseDTO) : this(
        person = PersonDb(individualExpenseDTO.person),
        expense = individualExpenseDTO.expense,
        isParticipant = individualExpenseDTO.isParticipant
    )

    constructor(individualExpenseDTO: IndividualExpense) : this(
        person = PersonDb(individualExpenseDTO.person),
        expense = individualExpenseDTO.expenseState,
        isParticipant = individualExpenseDTO.isParticipantState
    )

    fun toIndividualExpense() = IndividualExpense(
        person = person.toPerson(),
        expense = expense,
        isParticipant = isParticipant
    )
}