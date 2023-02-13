package com.mikkelthygesen.billsplit.features.main.add_group.wigets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Friend
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.features.main.add_group.AddGroupViewModel
import com.mikkelthygesen.billsplit.sampleFriends
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun FutureAddFriendDialog(
    modifier: Modifier = Modifier,
    group: Group
) {
    val addGroupViewModel: AddGroupViewModel = viewModel()
    val friendsFlow = addGroupViewModel.observeFriends().collectAsState(initial = emptyList())
    _FutureAddFriendDialog(
        modifier = modifier,
        friends = friendsFlow.value,
        showProfile = addGroupViewModel::showProfile,
        group = group
    )
}

@Composable
@SuppressLint("ComposableNaming")
fun _FutureAddFriendDialog(
    modifier: Modifier = Modifier,
    friends: List<Friend>,
    showProfile: () -> Unit,
    group: Group
) {
    var showAddFriendDialog by rememberSaveable {
        mutableStateOf(false)
    }
    if (showAddFriendDialog) {
        val acceptedFriends = friends.filterIsInstance<Friend.FriendAccepted>()
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
            onRefresh = {},
            onAddFriend = group::addPerson,
            onGoToProfilePage = {
                showAddFriendDialog = false
                showProfile()
            }
        )
    }
    SimpleIconButton(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_add_plus,
        tint = MaterialTheme.colorScheme.onPrimary
    ) {
        showAddFriendDialog = true
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    _FutureAddFriendDialog(friends = sampleFriends, showProfile = { }, group = sampleGroup)
}