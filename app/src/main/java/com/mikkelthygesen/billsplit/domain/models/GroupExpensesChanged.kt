package com.mikkelthygesen.billsplit.domain.models

import com.mikkelthygesen.billsplit.data.local.database.model.ExpenseChangeDb
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event

data class GroupExpensesChanged(
    override val id: String,
    override val createdBy: Person,
    val groupExpenseOriginal: GroupExpense,
    val groupExpenseEdited: GroupExpense,
    override val timeStamp: Long = System.currentTimeMillis(),
) : Event{
    constructor(expenseChangeDb: ExpenseChangeDb):this(
        id = expenseChangeDb.id,
        createdBy = Person(expenseChangeDb.createdBy),
        groupExpenseOriginal = expenseChangeDb.groupExpenseOriginal.toGroupExpense(),
        groupExpenseEdited = expenseChangeDb.groupExpenseEdited.toGroupExpense(),
        timeStamp = expenseChangeDb.timeStamp
    )
}