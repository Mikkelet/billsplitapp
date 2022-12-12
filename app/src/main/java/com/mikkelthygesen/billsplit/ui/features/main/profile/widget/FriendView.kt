package com.mikkelthygesen.billsplit.ui.features.main.profile.widget

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
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.widgets.CircularImageView
import com.mikkelthygesen.billsplit.ui.widgets.CircularUrlImageView
import com.mikkelthygesen.billsplit.ui.widgets.ClickableFutureComposable


@Composable
fun ProfilePageFriendView(
    friend: Friend,
    mainViewModel: MainViewModel = viewModel(),
) {
    var friendStatus by remember {
        mutableStateOf(friend)
    }

    _FriendView(person = friend.person) {
        when (friendStatus) {
            is Friend.FriendRequestReceived -> {
                ClickableFutureComposable(
                    asyncCallback = {
                        mainViewModel.acceptFriendRequest(friend.person)
                    },
                    onSuccess = {
                        if (it != null)
                            friendStatus = it
                    },
                    onError = mainViewModel::handleError,
                    loadingComposable = {
                        Text(text = "Accepting...")
                    }) {
                    Button(onClick = it) {
                        Text(text = "Accept")
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
            CircularUrlImageView(
                modifier = Modifier.size(64.dp),
                imageUrl = person.pfpUrlState
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
    ProfilePageFriendView(
        friend = Friend.FriendRequestSent(samplePeopleShera[1])
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAcceptRequest() {
    ProfilePageFriendView(
        friend = Friend.FriendRequestReceived(samplePeopleShera[1])
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriend() {
    ProfilePageFriendView(
        friend = Friend.FriendAccepted(samplePeopleShera[1])
    )
}