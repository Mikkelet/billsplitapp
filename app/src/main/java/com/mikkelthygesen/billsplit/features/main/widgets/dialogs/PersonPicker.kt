package com.mikkelthygesen.billsplit.features.main.widgets.dialogs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.PersonListItem

@Composable
fun PersonPicker(
    people: List<Person>,
    onSelect: (Person) -> Unit,
    listItem: @Composable (Person) -> Unit = {
        PersonListItem(person = it, onSelect = onSelect)
    },
) {
    LazyColumn(
        modifier = Modifier.shadowModifier(MaterialTheme.colors.background)
    ) {
        items(people.size) { index ->
            val person = people[index]
            listItem(person)
        }
    }
}
