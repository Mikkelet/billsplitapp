package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.ExpenseHolder.IndividualExpenseHolder

data class Debt(
    val payee: IndividualExpenseHolder,
    val sharedExpense: Float,
    val indebted: List<IndividualExpenseHolder>
)

