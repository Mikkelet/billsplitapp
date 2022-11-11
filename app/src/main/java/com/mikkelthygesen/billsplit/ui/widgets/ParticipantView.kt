package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.*
import com.mikkelthygesen.billsplit.Person

@Composable
fun ParticipantView(
    person: Person,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: ((Person) -> Unit)?,
    sharedOwed: Float,
    canEditName: Boolean
) {
    val totalOwed = person.owed + sharedOwed
    PersonView(
        person = person,
        onChangeListener = onChangeListener,
        onRemoveClicked = onRemoveClicked,
        owed = "$totalOwed",
        enableNameChange = canEditName
    )
}