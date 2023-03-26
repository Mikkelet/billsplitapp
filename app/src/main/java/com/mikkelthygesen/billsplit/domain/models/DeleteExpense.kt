package com.mikkelthygesen.billsplit.domain.models

import com.mikkelthygesen.billsplit.domain.models.interfaces.Event

data class DeleteExpense(
    override val id: String = "",
    override val createdBy: Person,
    override val timeStamp: Long = System.currentTimeMillis(),
    val expense: GroupExpense
) : Event