package com.mikkelthygesen.billsplit.models

import com.mikkelthygesen.billsplit.models.interfaces.IShareable

data class Payment(
    override val createdBy: Person,
    val paidTo: Person,
    val amount: Float,
    override val timeStamp: Long = System.currentTimeMillis(),
) : IShareable
