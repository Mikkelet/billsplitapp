package com.mikkelthygesen.billsplit.ui.features.main.add_group.wigets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.ClickableFriendView
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier


@Composable
fun AddFriendToGroupDialog(
    friendsToAdd: List<Person>,
    totalFriends: Int,
    onDismiss: () -> Unit,
    onGoToProfilePage: () -> Unit,
    onAddFriend: (Person) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        LazyColumn(
            modifier = Modifier.shadowModifier(MaterialTheme.colors.background)
        ) {
            if (friendsToAdd.isEmpty())
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        if (totalFriends == 0)
                            Text(text = "You have no friends to add. You can add friends in the profile page!")
                        else Text(text = "You have added all your friends! To add more friends, go to profile page!")
                        Button(
                            modifier = Modifier.padding(top = 32.dp),
                            onClick = onGoToProfilePage) {
                            Text(text = "Go to profile page!")
                        }
                    }
                }
            else
                items(friendsToAdd.size) { index ->
                    val friend = friendsToAdd[index]
                    ClickableFriendView(friend = friend) {
                        onAddFriend(friend)
                        onDismiss()
                    }
                }
        }
    }
}