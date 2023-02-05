package com.mikkelthygesen.billsplit

import android.util.Patterns
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mikkelthygesen.billsplit.domain.models.IndividualExpense
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

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

fun String.isFloat(): Boolean = try {
    toFloat()
    true
} catch (e: Exception) {
    false
}

fun String.parseToFloat(): Float = try {
    toFloat()
} catch (e: Exception) {
    0F
}

fun List<Float>.reduceOrZero() = if (isEmpty()) 0F else reduce { acc, fl -> acc + fl }

fun Float.fmt2dec() = if (rem(1F) != 0f) String.format("%,.2f", this) else "${this.toInt()}"

fun List<Person>.toNewIndividualExpenses() = map { IndividualExpense(it, 0F, true) }

fun String.matchesEmail(): Boolean =
    this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Fragment.collectEvents(
    flow: SharedFlow<BaseViewModel.UiEvent>,
    onEvent: (BaseViewModel.UiEvent) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.collect {
            onEvent(it)
        }
    }
}