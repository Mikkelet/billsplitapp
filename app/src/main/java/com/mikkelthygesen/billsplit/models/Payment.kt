package com.mikkelthygesen.billsplit.models

import com.mikkelthygesen.billsplit.models.interfaces.IShareable

data class Payment(
    val payee: Person,
    val paidTo: Person,
    val amount: Float
) : IShareable {
    override val timeStamp: Long = System.currentTimeMillis()
}
