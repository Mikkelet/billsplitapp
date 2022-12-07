package com.mikkelthygesen.billsplit.ui.features.main.profile.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun FriendView(
    user: String,
    friend: Friend,
    mainViewModel: MainViewModel = viewModel(),
) {
    var friendStatus by remember {
        mutableStateOf(friend)
    }
    var loading by remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    _FriendView(person = friend.person) {
        when (friendStatus) {
            is Friend.FriendRequestReceived -> {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Button(onClick = {
                        loading = true
                        coroutineScope.launch {
                            delay(2000L)
                            val response = kotlin.runCatching {
                                mainViewModel.acceptFriendRequest(user, friend.person)
                            }
                            response.fold(
                                onSuccess = {
                                    loading = false
                                    friendStatus = it
                                },
                                onFailure = ::println
                            )
                        }
                    }) {
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
@SuppressLint("ComposableNaming")
private fun _FriendView(
    person: Person,
    trailingView: @Composable () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularImageView(
                modifier = Modifier.size(64.dp),
                imageResId = person.pfpResId
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
        user = samplePeopleShera.first().uid,
        friend = Friend.FriendRequestSent(samplePeopleShera[1])
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewAcceptRequest() {
    FriendView(
        user = samplePeopleShera.first().uid,
        friend = Friend.FriendRequestReceived(samplePeopleShera[1])
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFriend() {
    FriendView(
        user = samplePeopleShera.first().uid,
        friend = Friend.FriendAccepted(samplePeopleShera[1])
    )
}