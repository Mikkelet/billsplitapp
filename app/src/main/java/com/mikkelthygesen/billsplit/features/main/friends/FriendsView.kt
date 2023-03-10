package com.mikkelthygesen.billsplit.features.main.friends

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.friends.views.AddFriendEmailTextField
import com.mikkelthygesen.billsplit.features.main.friends.views.FriendView
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun FriendsView() {
    val friendsViewModel: FriendsViewModel = viewModel()
    val friendsUiStateFlow = friendsViewModel.uiStateFlow.collectAsState()
    val friendsUiState = friendsUiStateFlow.value
    val isLoading = friendsUiState is BaseViewModel.UiState.Loading

    val friendsFlow = friendsViewModel.observeLocalFriends().collectAsState(initial = emptyList())
    val friendsState = friendsFlow.value

    FriendsListWithTitle(
        friends = if (isLoading)
            emptyList() else friendsState.sortedBy { it.javaClass.simpleName },
        showLoading = isLoading,
        onRefreshClick = {
            friendsViewModel.getFriends()
        },
        onBackClicked = friendsViewModel::onBackButtonPressed
    )
}

@Composable
private fun FriendsListWithTitle(
    friends: List<Friend>,
    showLoading: Boolean = false,
    onRefreshClick: () -> Unit,
    onBackClicked: () -> Unit
) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {
        BigTopBar(leadingContent = {
            BackButton {
                onBackClicked()
            }
        })
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 32.dp, start = 32.dp),
                text = "Friends",
                style = MaterialTheme.typography.headlineMedium
            )
            SimpleIconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
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
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp)
            .shadowModifier(
                backgroundColor = listItemColor(),
                outerPadding = PaddingValues(0.dp)
            ),
    ) {
        AddFriendEmailTextField()
        friends.map {
            FriendView(it)
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
    FriendsListWithTitle(friends = friends, onBackClicked = {}, onRefreshClick = {})
}