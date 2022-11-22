package com.mikkelthygesen.billsplit.models

import com.mikkelthygesen.billsplit.models.interfaces.IShareable
import kotlin.random.Random

data class GroupExpensesChanged(
    override val createdBy: Person,
    val groupExpenseOriginal: GroupExpense,
    val groupExpenseEdited: GroupExpense,
    override val timeStamp: Long = System.currentTimeMillis().plus(Random(100L).nextLong())
) : IShareable