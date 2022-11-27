package com.mikkelthygesen.billsplit.ui.features.main.add_group

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person


@Composable
fun AddGroupView(group: Group) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Group name")
        TextField(value = group.nameState, onValueChange = {
            group.nameState = it
        })
        Box(modifier = Modifier.height(20.dp))
        Text(text = "People")
        group.peopleState.map { person ->
            Text(text = person.nameState)
        }
        Button(onClick = {
            group.addPerson(Person(name = "New person"))
        }) {
            Text(text = "Add person")
        }
    }
}
