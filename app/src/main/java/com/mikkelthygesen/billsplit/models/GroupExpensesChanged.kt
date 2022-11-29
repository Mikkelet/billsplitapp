package com.mikkelthygesen.billsplit.models

import com.mikkelthygesen.billsplit.models.interfaces.Event

data class GroupExpensesChanged(
    override val id: String,
    override val createdBy: Person,
    val groupExpenseOriginal: GroupExpense,
    val groupExpenseEdited: GroupExpense,
    override val timeStamp: Long = System.currentTimeMillis(),
) : Event