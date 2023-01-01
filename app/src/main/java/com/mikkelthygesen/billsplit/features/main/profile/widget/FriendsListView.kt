package com.mikkelthygesen.billsplit.features.main.profile.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.FutureState
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun FriendsListView(
    mainViewModel: MainViewModel = viewModel()
) {
    var sync by remember {
        mutableStateOf(false)
    }
    var friends: List<Friend> by remember {
        mutableStateOf(emptyList())
    }
    FutureComposable(
        asyncCallback = {
            sync = false
            mainViewModel.getFriends(false)
        },
        refreshCallback = {
            sync = true
            mainViewModel.getFriends(true)
        }
    ) { state, refresh ->
        when (state) {
            is FutureState.Loading -> FriendsListWithTitle(friends = emptyList(), sync) {}
            else -> {
                if (state is FutureState.Failure)
                    mainViewModel.handleError(state.error)
                else if (state is FutureState.Success)
                    friends = state.data
                FriendsListWithTitle(friends = friends.sortedBy { it.javaClass.simpleName }) {
                    refresh()
                }
            }
        }

    }
}

@Composable
private fun FriendsListWithTitle(
    friends: List<Friend>,
    showLoading: Boolean = false,
    onRefreshClick: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Friends",
                style = MaterialTheme.typography.h5
            )
            SimpleIconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp),
                iconResId = R.drawable.ic_baseline_refresh_24
            ) {
                onRefreshClick()
            }

        }
        _FriendsListView(friends = friends, showLoading)
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun _FriendsListView(
    friends: List<Friend>,
    showLoading: Boolean = false
) {
    var friendsState by remember {
        mutableStateOf(friends)
    }
    Column(
        modifier =
        Modifier.shadowModifier(MaterialTheme.colors.listItemColor()),
    ) {
        AddFriendEmailTextField {
            friendsState = friendsState.plus(it)
        }
        friendsState.map {
            ProfilePageFriendView(it)
        }
        if (showLoading)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val friends = (0..3).map {
        val person = Person("$it", "Person $it")
        Friend.FriendAccepted(person)
    }
    FriendsListWithTitle(friends = friends) {

    }
}