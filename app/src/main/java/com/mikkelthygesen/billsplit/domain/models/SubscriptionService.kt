package com.mikkelthygesen.billsplit.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mikkelthygesen.billsplit.data.local.database.model.SubscriptionServiceDb
import com.mikkelthygesen.billsplit.data.remote.dto.ServiceDTO

data class SubscriptionService(
    val id: String,
    private val name: String,
    val createdBy: Person,
    val imageUrl: String,
    private val monthlyExpense: Float,
    private val payer: Person,
    private val participants: List<Person>
) {

    constructor(serviceDTO: ServiceDTO): this(
        id = serviceDTO.id,
        name = serviceDTO.name,
        createdBy = Person(serviceDTO.createdBy),
        imageUrl = serviceDTO.imageUrl,
        monthlyExpense = serviceDTO.monthlyExpense,
        payer = Person(serviceDTO.payer),
        participants = serviceDTO.participants.map { Person(it) }
    )

    constructor(serviceDb: SubscriptionServiceDb): this(
        id = serviceDb.id,
        name = serviceDb.name,
        createdBy = Person(serviceDb.createdBy),
        imageUrl = serviceDb.imageUrl,
        monthlyExpense = serviceDb.monthlyExpense,
        payer = Person(serviceDb.payer),
        participants = serviceDb.participants.map { Person(it) }
    )

    var payerState by mutableStateOf(payer)
    var monthlyExpenseState by mutableStateOf(monthlyExpense)
    var nameState by mutableStateOf(name)
    var participantsState by mutableStateOf(participants)

    fun removeParticipant(person: Person) {
        participantsState = participantsState.minus(person)
    }

    fun addParticipant(person: Person) {
        participantsState = participantsState.plus(person)
    }
}