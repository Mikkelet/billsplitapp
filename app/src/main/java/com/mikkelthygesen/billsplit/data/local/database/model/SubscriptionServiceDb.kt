package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.data.remote.dto.ServiceDTO
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService

@Entity(tableName = "services")
data class SubscriptionServiceDb(
    @PrimaryKey
    val id: String,
    val name: String,
    @Embedded("createdBy_")
    val createdBy: PersonDb,
    val imageUrl: String,
    val monthlyExpense: Float,
    @Embedded("payer_")
    val payer: PersonDb,
    val participants: List<PersonDb>
) {

    constructor(subscriptionService: SubscriptionService) : this(
        id = subscriptionService.id,
        name = subscriptionService.nameState,
        createdBy = PersonDb(subscriptionService.createdBy),
        imageUrl = subscriptionService.imageUrl,
        monthlyExpense = subscriptionService.monthlyExpenseState,
        payer = PersonDb(subscriptionService.payerState),
        participants = subscriptionService.participantsState.map { PersonDb(it) }
    )

    constructor(serviceDTO: ServiceDTO):this(
        id = serviceDTO.id,
        name = serviceDTO.name,
        createdBy = PersonDb(serviceDTO.createdBy),
        monthlyExpense = serviceDTO.monthlyExpense,
        payer = PersonDb(serviceDTO.payer),
        imageUrl = serviceDTO.imageUrl,
        participants = serviceDTO.participants.map { PersonDb(it) }
    )
}