package com.mikkelthygesen.billsplit.features.main.add_group.wigets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.sampleFriends
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.ui.widgets.ErrorView
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.FutureState
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton


@Composable
fun FutureAddFriendDialog(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    group: Group
) {
    _FutureAddFriendDialog(
        modifier = modifier,
        getFriends = mainViewModel::getFriends,
        showProfile = mainViewModel::showProfile,
        group = group
    )
}

@Composable
@SuppressLint("ComposableNaming")
fun _FutureAddFriendDialog(
    modifier: Modifier = Modifier,
    getFriends: suspend (Boolean) -> List<Friend>,
    showProfile: () -> Unit,
    group: Group
) {
    var showAddFriendDialog by remember {
        mutableStateOf(false)
    }
    if (showAddFriendDialog) {
        FutureComposable(
            asyncCallback = { getFriends(false) },
        ) { state, refresh ->
            when (state) {
                is FutureState.Loading -> {
                    Dialog(onDismissRequest = {
                        showAddFriendDialog = false
                    }) {
                        CircularProgressIndicator()
                    }
                }
                is FutureState.Failure -> ErrorView(error = state.error, refresh)
                is FutureState.Success -> {
                    val acceptedFriends = state.data.filterIsInstance<Friend.FriendAccepted>()
                    val addableFriends = acceptedFriends
                        .let { list ->
                            if (list.isEmpty()) emptyList()
                            else list.map { it.person }
                                .minus(group.peopleState.toSet())

                        }
                    AddFriendToGroupDialog(
                        friendsToAdd = addableFriends,
                        totalFriends = acceptedFriends.size,
                        onDismiss = {
                            showAddFriendDialog = false
                        },
                        onRefresh = refresh,
                        onAddFriend = group::addPerson,
                        onGoToProfilePage = {
                            showAddFriendDialog = false
                            showProfile()
                        }
                    )
                }
            }

        }


    }
    SimpleIconButton(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary),
        iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_add_plus,
        tint = MaterialTheme.colors.onPrimary
    ) {
        showAddFriendDialog = true
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    _FutureAddFriendDialog(getFriends = { sampleFriends }, showProfile = { }, group = sampleGroup)
}