package com.mikkelthygesen.billsplit.domain.models

import com.mikkelthygesen.billsplit.domain.models.interfaces.Event

data class GroupExpensesChanged(
    override val id: String,
    override val createdBy: Person,
    val groupExpenseOriginal: GroupExpense,
    val groupExpenseEdited: GroupExpense,
    override val timeStamp: Long = System.currentTimeMillis(),
) : Event