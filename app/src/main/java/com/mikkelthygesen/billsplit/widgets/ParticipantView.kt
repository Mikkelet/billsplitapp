package com.mikkelthygesen.billsplit.widgets

import androidx.compose.runtime.*
import com.mikkelthygesen.billsplit.Person

@Composable
fun ParticipantView(
    person: Person,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: () -> Unit,
    sharedOwed: Float
) {
    val totalOwed = person.owed + sharedOwed
    PersonView(
        person = person,
        onChangeListener = onChangeListener,
        onRemoveClicked = onRemoveClicked,
        owed = "$totalOwed"
    )
}