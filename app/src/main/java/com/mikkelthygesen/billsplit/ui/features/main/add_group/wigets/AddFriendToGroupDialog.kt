package com.mikkelthygesen.billsplit.ui.features.main.add_group.wigets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.ClickableFriendView
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier


@Composable
fun AddFriendToGroupDialog(
    friends: List<Person>,
    onDismiss: () -> Unit,
    onAddFriend: (Person) -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        LazyColumn(
            modifier = Modifier.shadowModifier(MaterialTheme.colors.background)
        ) {
            if (friends.isEmpty())
                item {
                    Text(text = "You have added all your friends")
                }
            else
                items(friends.size) { index ->
                    val friend = friends[index]
                    ClickableFriendView(friend = friend) {
                        onAddFriend(friend)
                        onDismiss()
                    }
                }
        }
    }
}