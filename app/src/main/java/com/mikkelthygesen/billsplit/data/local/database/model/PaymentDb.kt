package com.mikkelthygesen.billsplit.data.local.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import com.mikkelthygesen.billsplit.models.Payment

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
) {

    fun toPayment() = Payment(
        id = id,
        createdBy = createdBy.toPerson(),
        paidTo = paidTo.toPerson(),
        amount = amount,
        timeStamp = timeStamp
    )
}