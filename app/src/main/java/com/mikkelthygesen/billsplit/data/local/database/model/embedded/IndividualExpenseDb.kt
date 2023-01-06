package com.mikkelthygesen.billsplit.data.local.database.model.embedded

import com.mikkelthygesen.billsplit.domain.models.IndividualExpense

@kotlinx.serialization.Serializable
data class IndividualExpenseDb(
    val person: PersonDb,
    val expense: Float,
    val isParticipant: Boolean
){

    fun toIndividualExpense() = IndividualExpense(
        person = person.toPerson(),
        expense = expense,
        isParticipant = isParticipant
    )
}