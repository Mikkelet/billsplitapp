package com.mikkelthygesen.billsplit.ui.features.main.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(user)
        FriendsView(user = user, friends = friends)
    }
}

@Composable
fun FriendsView(user: Person, friends: List<Friend>) {
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
                .padding(16.dp),
        ) {
            item {
                Text(text = "Friends", style = TextStyle(fontSize = 30.sp))
            }
            item {
                OutlinedTextField(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(), value = "Add friend", onValueChange = {
                })
            }
            items(friends.size) { index ->
                val friend = friends[index]
                FriendView(user = user.uid, friend = friend)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val friends = (0..3).map {
        val person = Person("$it", "Person $it")
        Friend.FriendAccepted(person)
    }
    _ProfileView(Person(name = "Catra"), friends)
}