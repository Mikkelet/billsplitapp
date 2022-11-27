package com.mikkelthygesen.billsplit

import com.mikkelthygesen.billsplit.models.IndividualExpense
import com.mikkelthygesen.billsplit.models.Person

fun <T> tryCatchDefault(defaultValue: T, callback: () -> T) = try {
    callback()
} catch (e: Exception) {
    println(e)
    defaultValue
}

fun tryParseToFloat(expenseHolder: IndividualExpense, value: String) = try {
    expenseHolder.expenseState = value.toFloat()
    true
} catch (e: Exception) {
    false
}

fun List<Float>.reduceOrZero() = if (isEmpty()) 0F else reduce { acc, fl -> acc + fl }

fun Float.fmt2dec() = if(rem(1F) != 0f) String.format("%,.2f", this) else "${this.toInt()}"

fun List<Person>.toNewIndividualExpenses() = map { IndividualExpense(it, 0F, true) }