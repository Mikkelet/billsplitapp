package com.mikkelthygesen.billsplit.widgets

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.Person

@Composable
fun SharedExpensesView(
    shared: Person,
    onChangeListener: (Float) -> Unit
) {
    PersonView(
        person = shared,
        onChangeListener = onChangeListener,
        onRemoveClicked = null,
        owed = "${shared.owed}"
    )
}