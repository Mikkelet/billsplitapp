package com.mikkelthygesen.billsplit.ui.features.main.add_group

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel

@Composable
fun AddGroupView(
    viewModel: MainViewModel = viewModel(),
    group: Group
) {
    _AddGroupView(group = group, onClick = {
        viewModel.saveGroup(group)
    })
}


@SuppressLint("ComposableNaming")
@Composable
fun _AddGroupView(
    group: Group,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Group name, createdBy = ${group.createdBy.nameState}")
        TextField(value = group.nameState, onValueChange = {
            group.nameState = it
        })
        Box(modifier = Modifier.height(20.dp))
        Text(text = "People")
        if (group.peopleState.isNotEmpty())
            group.peopleState.map { person ->
                Text(text = person.nameState)
            }
        Button(onClick = {
            group.addPerson(Person(name = "New person"))
        }) {
            Text(text = "Add person")
        }
        Button(
            onClick = onClick
        ) {
            Text(text = "Add group!")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    _AddGroupView(group = Group("asd","My Group", samplePeopleShera, samplePeopleShera.first())) {
        
    }
}