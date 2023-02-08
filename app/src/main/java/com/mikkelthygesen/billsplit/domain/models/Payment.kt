package com.mikkelthygesen.billsplit.domain.models

import com.mikkelthygesen.billsplit.data.local.database.model.PaymentDb
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event

data class Payment(
    override val id: String = "",
    override val createdBy: Person,
    val paidTo: Person,
    val amount: Float,
    override val timeStamp: Long = System.currentTimeMillis(),
) : Event {

    constructor(paymentDb: PaymentDb) : this(
        id = paymentDb.id,
        createdBy = Person(paymentDb.createdBy),
        paidTo = Person(paymentDb.paidTo),
        amount = paymentDb.amount,
        timeStamp = paymentDb.timeStamp
    )

    fun asExpense() = GroupExpense(
        id = id,
        createdBy = createdBy,
        payee = createdBy,
        individualExpenses = listOf(
            IndividualExpense(paidTo, amount, true)
        ),
        timeStamp = timeStamp
    )
}
