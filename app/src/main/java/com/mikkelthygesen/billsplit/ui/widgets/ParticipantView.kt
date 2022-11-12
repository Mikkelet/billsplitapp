package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.mikkelthygesen.billsplit.Person

@Composable
fun ParticipantView(
    person: Person,
    onChangeListener: (Float) -> Unit,
    onRemoveClicked: (Person) -> Unit,
    sharedOwed: Float
) {
    val totalOwed = if (person.isParticipant)
        person.owed + sharedOwed else 0F
    PersonView(
        person = person,
        onChangeListener = onChangeListener,
        onRemoveClicked = onRemoveClicked,
        owed = "$totalOwed",
        flags = PersonViewFlags.participant()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewParticipantView() {
    val person = Person("Person 1", 1000F)
    ParticipantView(
        person = person,
        onChangeListener = {},
        onRemoveClicked = {},
        sharedOwed = 100F
    )
}