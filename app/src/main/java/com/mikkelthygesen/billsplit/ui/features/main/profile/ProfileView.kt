package com.mikkelthygesen.billsplit.ui.features.main.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.ProfilePageFriendView
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.ui.features.main.widgets.ProfileHeader
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable

@Composable
fun ProfileView(
    mainViewModel: MainViewModel = viewModel()
) {
    FutureComposable(
        asyncCallback = {
            val friends = mainViewModel.getFriends()
            Pair(mainViewModel.requireLoggedInUser, friends)
        }) {
        _ProfileView(user = it.first, friends = it.second)
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun _ProfileView(user: Person, friends: List<Friend>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(user)
        FriendsView(friends)
    }
}

@Composable
fun FriendsView(friends: List<Friend>) {
    Column(
        modifier = shadowModifier(MaterialTheme.colors.background),
    ) {
        Text(text = "Friends", style = TextStyle(fontSize = 30.sp))
        OutlinedTextField(modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(), value = "Add friend", onValueChange = {
        })
        friends.map {
            ProfilePageFriendView(it)
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