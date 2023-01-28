package com.mikkelthygesen.billsplit.features.main.friends.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.main.friends.FriendsViewModel
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture
import com.mikkelthygesen.billsplit.ui.widgets.TriggerFutureState


@Composable
fun FriendView(
    friend: Friend,
    friendsViewModel: FriendsViewModel = viewModel(),
) {
    var friendStatus by remember {
        mutableStateOf(friend)
    }

    _FriendView(person = friend.person) {
        when (friendStatus) {
            is Friend.FriendRequestReceived -> {
                TriggerFutureComposable(
                    onClickAsync = {
                        friendsViewModel.acceptFriendRequest(friend.person)
                    }) { state, addFriend ->
                    when (state) {
                        is TriggerFutureState.Loading -> Text(text = "Accepting...")
                        else -> {
                            if (state is TriggerFutureState.Success) {
                                friendStatus = state.data
                            } else if (state is TriggerFutureState.Failure)
                                friendsViewModel.handleError(state.error)
                            Button(onClick = addFriend) {
                                Text(text = "Accept")
                            }
                        }
                    }
                }
            }
            is Friend.FriendRequestSent -> {
                Text(
                    text = "Request sent",
                    style = MaterialTheme.typography.subtitle1.copy(color = Color.Gray)
                )
            }
            is Friend.FriendAccepted -> {}
        }
    }
}

@Composable
fun ClickableFriendView(
    friend: Person,
    trailingView: () -> Unit = { },
    onClick: () -> Unit,
) {
    _FriendView(
        modifier = Modifier.clickable {
            onClick()
        },
        person = friend
    ) {
        trailingView()
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun _FriendView(
    modifier: Modifier = Modifier,
    person: Person,
    trailingView: @Composable () -> Unit = {}
) {
    Row(
        modifier
            .fillMaxWidth()
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
                text = person.nameState,
                style = MaterialTheme.typography.subtitle2
            )
        }
        trailingView()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriendRequestSent() {
    FriendView(
        friend = Friend.FriendRequestSent(samplePeopleShera[1])
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAcceptRequest() {
    FriendView(
        friend = Friend.FriendRequestReceived(samplePeopleShera[1])
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriend() {
    FriendView(
        friend = Friend.FriendAccepted(samplePeopleShera[1])
    )
}