package com.mikkelthygesen.billsplit

import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.mikkelthygesen.billsplit.domain.models.IndividualExpense
import kotlinx.coroutines.flow.Flow
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

fun String.matchesEmail(): Boolean =
    this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun <T> Fragment.collectEvents(
    flow: Flow<T>,
    onEvent: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.collect {
            onEvent(it)
        }
    }
}

fun Context.getActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}