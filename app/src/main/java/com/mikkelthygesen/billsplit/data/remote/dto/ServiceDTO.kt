package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.domain.models.SubscriptionService

@kotlinx.serialization.Serializable
data class ServiceDTO(
    val id: String,
    val name: String,
    val createdBy: PersonDTO,
    val imageUrl: String,
    val monthlyExpense: Float,
    val payer: PersonDTO,
    val participants: List<PersonDTO>
) {
    constructor(service: SubscriptionService) : this(
        id = service.id,
        name = service.nameState,
        createdBy = PersonDTO(service.createdBy),
        imageUrl = service.imageUrl,
        monthlyExpense = service.monthlyExpenseState,
        payer = PersonDTO(service.payerState),
        participants = service.participantsState.map { PersonDTO(it) },
    )
}