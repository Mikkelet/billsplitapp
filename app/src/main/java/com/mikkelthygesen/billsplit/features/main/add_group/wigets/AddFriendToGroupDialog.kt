package com.mikkelthygesen.billsplit.features.main.add_group.wigets

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.mikkelthygesen.billsplit.features.main.profile.widget.ClickableFriendView
import com.mikkelthygesen.billsplit.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.GenericDialog


@Composable
fun AddFriendToGroupDialog(
    friendsToAdd: List<Person>,
    totalFriends: Int,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit,
    onGoToProfilePage: () -> Unit,
    onAddFriend: (Person) -> Unit,
) {

    if (friendsToAdd.isEmpty()) {
        val noFriendsText = if (totalFriends == 0)
            "You have no friends to add. You can add friends in the profile page or try to refresh."
        else "You have added all your friends! To add more friends, go to profile page or you can also try to refresh."

        GenericDialog(
            dialogText = noFriendsText,
            primaryText = "Go to profile page!",
            primaryAction = onGoToProfilePage,
            secondaryText = "Refresh",
            secondaryAction = onRefresh,
            onDismiss = onDismiss
        )
    } else
        Dialog(onDismissRequest = onDismiss) {
            LazyColumn(
                modifier = Modifier.shadowModifier(MaterialTheme.colors.background)
            ) {
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