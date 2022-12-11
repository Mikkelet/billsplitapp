package com.mikkelthygesen.billsplit.ui.features.main.add_group

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.add_group.wigets.AddFriendToGroupDialog
import com.mikkelthygesen.billsplit.ui.widgets.ClickableFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable

@Composable
fun AddGroupView(
    viewModel: MainViewModel = viewModel(),
) {
    FutureComposable(
        asyncCallback = {
            Pair(viewModel.requireLoggedInUser, viewModel.getFriends())
        },
        onError = viewModel::handleError
    ) {
        val group = Group(
            id = "",
            name = "My group",
            people = listOf(it.first),
            createdBy = it.first,
            timeStamp = 0,
            events = listOf()
        )
        val acceptedFriends = it.second.filterIsInstance<Friend.FriendAccepted>()
            .map { friendAccepted -> friendAccepted.person }
        _AddGroupView(group = group, friends = acceptedFriends, onAddGroup = {
            viewModel.saveGroup(group)
        })
    }
}


@SuppressLint("ComposableNaming")
@Composable
fun _AddGroupView(
    group: Group,
    onAddGroup: suspend () -> Unit,
    friends: List<Person>
) {
    var showAddFriendDialog by remember {
        mutableStateOf(false)
    }
    if (showAddFriendDialog) {

        AddFriendToGroupDialog(friends = friends.minus(group.peopleState.toSet()),
            onDismiss = {
                showAddFriendDialog = false
            },
            onAddFriend = {
                group.addPerson(it)
            })
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Group name, createdBy = ${group.createdBy.nameState}")
        TextField(value = group.nameState, onValueChange = {
            group.nameState = it
        })
        ClickableFutureComposable(asyncCallback = onAddGroup) {
            Button(onClick = {
                if (group.nameState.isNotBlank() && group.peopleState.isNotEmpty())
                    it.invoke()
            }) {
                Text(text = "Add group")
            }
        }
        Box(modifier = Modifier.height(20.dp))
        Text(text = "People")
        if (group.peopleState.isNotEmpty())
            group.peopleState.map { person ->
                Text(text = person.nameState)
            }
        Button(onClick = {
            showAddFriendDialog = true
        }) {
            Text(text = "Add friend")
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    _AddGroupView(
        group = Group("asd", "My Group", samplePeopleShera, samplePeopleShera.first()),
        friends = samplePeopleShera,
        onAddGroup = {}
    )
}