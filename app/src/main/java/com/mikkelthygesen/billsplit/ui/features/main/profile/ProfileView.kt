package com.mikkelthygesen.billsplit.ui.features.main.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.AcceptFriend
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.FriendRequestSent
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.FriendView
import com.mikkelthygesen.billsplit.ui.features.main.widgets.ProfileHeader

@Composable
fun ProfileView(
    user: Person,
    friends: List<Friend>,
    mainViewModel: MainViewModel = viewModel()
) {
    _ProfileView(user = user, friends = friends)
}

@Composable
@SuppressLint("ComposableNaming")
private fun _ProfileView(user: Person, friends: List<Friend>) {
    Column {
        ProfileHeader(user)
        FriendsView(friends = friends)
    }
}

@Composable
fun FriendsView(friends: List<Friend>) {
    Column {
        Text(text = "Friends", style = TextStyle(fontSize = 30.sp))
        LazyColumn {
            items(friends.size) { index ->
                when (val friend = friends[index]) {
                    is Friend.FriendAccepted -> FriendView(friend.person)
                    is Friend.FriendRequestSent -> FriendRequestSent(friend.person)
                    is Friend.FriendRequestReceived -> AcceptFriend(person = friend.person)
                }
            }
        }
        TextField(value = "Add friend", onValueChange = {

        })
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    _ProfileView(Person(), emptyList())
}