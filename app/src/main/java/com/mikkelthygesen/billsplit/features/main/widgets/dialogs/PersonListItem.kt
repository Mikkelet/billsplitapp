package com.mikkelthygesen.billsplit.features.main.widgets.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture


@Composable
fun PersonListItem(
    modifier: Modifier = Modifier,
    person: Person,
    text: String = person.nameState,
    onSelect: ((Person) -> Unit)? = null,
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
            .let {
                if (onSelect == null) it
                else it.clickable { onSelect(person) }
            }
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfilePicture(
                modifier = Modifier.size(64.dp),
                person = person
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Box {
            trailingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PersonListItem(person = samplePeopleShera[0], onSelect = {}) {
        Icon(Icons.Filled.Menu, contentDescription = "")
    }
}