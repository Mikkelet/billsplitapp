package com.mikkelthygesen.billsplit.ui.features.main.add_group.wigets

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Friend
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun FutureAddFriendDialog(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    group: Group
) {
    var showAddFriendDialog by remember {
        mutableStateOf(false)
    }
    if (showAddFriendDialog) {
        FutureComposable(
            asyncCallback = mainViewModel::getFriends,
            loadingComposable = {
                Dialog(onDismissRequest = {
                    showAddFriendDialog = false
                }) {
                    CircularProgressIndicator()
                }
            }
        ) { friends, refreshCallback ->
            val acceptedFriends =
                friends.filterIsInstance<Friend.FriendAccepted>()
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
                onRefresh = {
                    refreshCallback { mainViewModel.getFriends(true) }
                },
                onAddFriend = group::addPerson,
                onGoToProfilePage = {
                    showAddFriendDialog = false
                    mainViewModel.showProfile()
                }
            )
        }
    }
    SimpleIconButton(
        modifier = modifier
            .clip(RoundedCornerShape(45.dp))
            .background(MaterialTheme.colors.primary),
        iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_add_plus,
        color = MaterialTheme.colors.onPrimary
    ) {
        showAddFriendDialog = true
    }
}