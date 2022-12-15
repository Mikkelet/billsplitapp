package com.mikkelthygesen.billsplit.ui.features.main.profile.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView

@Composable
fun FriendsListView(
    mainViewModel: MainViewModel = viewModel()
) {
    FutureComposable(
        asyncCallback = { mainViewModel.getFriends() },
        onError = mainViewModel::handleError,
        loadingComposable = {
            _FriendsListView(friends = emptyList(), true)
        }
    ) {
        _FriendsListView(friends = it)
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun _FriendsListView(friends: List<Friend>, showLoading: Boolean = false) {
    var friendsState by remember {
        mutableStateOf(friends)
    }
    Column(
        modifier = shadowModifier(MaterialTheme.colors.background),
    ) {
        AddFriendEmailTextField {
            friendsState = friendsState.plus(it)
        }
        friendsState.map {
            ProfilePageFriendView(it)
        }
        if(showLoading)
            LoadingView()
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val friends = (0..3).map {
        val person = Person("$it", "Person $it")
        Friend.FriendAccepted(person)
    }
    _FriendsListView(friends = friends)
}