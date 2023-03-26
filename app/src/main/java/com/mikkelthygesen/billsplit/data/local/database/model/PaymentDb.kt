package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.data.remote.dto.EventDTO
import com.mikkelthygesen.billsplit.domain.models.Payment

@Entity(tableName = "payments")
data class PaymentDb(
    @PrimaryKey
    val id: String,
    val groupId: String,
    @Embedded(prefix = "createdby_")
    val createdBy: PersonDb,
    @Embedded(prefix = "paidto_")
    val paidTo: PersonDb,
    val amount: Float,
    val timeStamp: Long,
) : EventDb {

    constructor(groupId: String, paymentDTO: EventDTO.PaymentDTO) : this(
        groupId = groupId,
        id = paymentDTO.id,
        createdBy = PersonDb(paymentDTO.createdBy),
        timeStamp = paymentDTO.timeStamp,
        paidTo = PersonDb(paymentDTO.paidTo),
        amount = paymentDTO.amount,
    )

    constructor(groupId: String, paymentDTO: Payment) : this(
        groupId = groupId,
        id = paymentDTO.id,
        createdBy = PersonDb(paymentDTO.createdBy),
        timeStamp = paymentDTO.timeStamp,
        paidTo = PersonDb(paymentDTO.paidTo),
        amount = paymentDTO.amount,
    )
}