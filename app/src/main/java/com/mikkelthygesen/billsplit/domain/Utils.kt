package com.mikkelthygesen.billsplit.domain

import com.mikkelthygesen.billsplit.data.local.database.BillSplitDb
import com.mikkelthygesen.billsplit.data.local.database.model.EventDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb

suspend fun GroupDb.latestEvent(database: BillSplitDb): EventDb? {
    return database.let {
        val id = latestEvent.id
        if (id == null) null
        else when (latestEvent.type) {
            GroupDb.EVENT_TYPE_PAYMENT -> it.paymentsDao()
                .getPayment(id)
            GroupDb.EVENT_TYPE_EXPENSE -> it.groupExpensesDao()
                .getGroupExpense(id)
            GroupDb.EVENT_TYPE_CHANGE -> it.expenseChangesDao()
                .getExpenseChange(id)
            else -> null
        }
    }
}