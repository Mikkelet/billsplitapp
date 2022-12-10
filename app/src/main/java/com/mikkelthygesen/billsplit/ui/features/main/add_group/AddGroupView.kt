package com.mikkelthygesen.billsplit.ui.features.main.add_group

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.AddGroupFriendView

@Composable
fun AddGroupView(
    viewModel: MainViewModel = viewModel(),
    group: Group,
    friends: List<Person>
) {
    _AddGroupView(group = group, friends = friends, onClick = {
        viewModel.saveGroup(group)
    })
}


@SuppressLint("ComposableNaming")
@Composable
fun _AddGroupView(
    group: Group,
    onClick: () -> Unit,
    friends: List<Person>
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            items(friends.size) { index ->
                val friend = friends[index]
                val selected = group.peopleState.contains(friend)
                AddGroupFriendView(friend = friend, selected = selected) { selectedState ->
                    if (selectedState) group.addPerson(friend)
                    else group.removePerson(friend)
                }
            }
        }
    }
}

@Composable
private fun AddFriendToGroupDialog(friends: List<Person>, onClick: (Person) -> Unit) {
    Dialog(onDismissRequest = { }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            items(friends.size) { index ->
                val friend = friends[index]
                AddGroupFriendView(friend = friend, selected = false) { selectedState ->
                    onClick(friend)
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    _AddGroupView(
        group = Group("asd", "My Group", samplePeopleShera, samplePeopleShera.first()),
        onClick = {},
        friends = samplePeopleShera
    )
}