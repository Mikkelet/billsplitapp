package com.mikkelthygesen.billsplit.models

import com.mikkelthygesen.billsplit.models.interfaces.IShareable

data class GroupExpensesChanged(
    val editor: Person,
    val groupExpenseOriginal: GroupExpense,
    val groupExpenseEdited: GroupExpense,
    override val timeStamp: Long = System.currentTimeMillis()
): IShareable {
}