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
    expenseHolder.expenseState = value.toFloat()
    true
} catch (e: Exception) {
    false
}


fun Modifier.paddingBottom(padding: Dp) = this.padding(0.dp, 0.dp, 0.dp, padding)
fun Modifier.paddingEnd(padding: Dp) = this.padding(0.dp, 0.dp, padding, 0.dp)
fun Modifier.paddingStart(padding: Dp) = this.padding(padding, 0.dp, 0.dp, 0.dp)
fun Modifier.paddingTop(padding: Dp) = this.padding(0.dp, padding, 0.dp, 0.dp)

fun List<Float>.reduceOrZero() = if (isEmpty()) 0F else reduce { acc, fl -> acc + fl }