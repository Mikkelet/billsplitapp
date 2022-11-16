package com.mikkelthygesen.billsplit

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.models.ExpenseHolder

fun <T> tryCatchDefault(defaultValue: T, callback: () -> T) = try {
    callback()
} catch (e: Exception) {
    println(e)
    defaultValue
}

fun tryParseToFloat(expenseHolder: ExpenseHolder, value: String) = try {
    expenseHolder.expense = value.toFloat()
    false
} catch (e: Exception) {
    true
}


fun Modifier.paddingOnlyBottom(padding: Dp) = this.padding(0.dp, 0.dp, 0.dp, padding)