package com.mikkelthygesen.billsplit.features.main.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.services.views.CenteredMessage
import com.mikkelthygesen.billsplit.features.main.groups.views.GroupsTitle
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.features.main.widgets.GroupListItem
import com.mikkelthygesen.billsplit.ui.widgets.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupsList(
    uiState: BaseViewModel.UiState,
) {
    val groupsViewModel: GroupsViewModel = viewModel()
    val groupsFlow = groupsViewModel.observeGroups().collectAsState(initial = emptyList())
    val groups = groupsFlow.value.sortedBy { it.nameState }
    val user = groupsViewModel.requireLoggedInUser

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState is BaseViewModel.UiState.Loading,
        onRefresh = { groupsViewModel.syncGroups() },
        refreshingOffset = 80.dp
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshState)
    ) {
        PullRefreshIndicator(
            refreshing = uiState is BaseViewModel.UiState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
        LazyColumn {
            item {
                GroupsTopBar()
            }
            item {
                GroupsTitle(user = user)
            }
            if (groups.isEmpty())
                item {
                    CenteredMessage(
                        modifier = Modifier.padding(vertical = 80.dp),
                        text = "Click + below to add a new group!")
                }
            else
                items(groups.size) { index ->
                    val group = groups[index]
                    GroupListItem(user, group = group) {
                        groupsViewModel.onGroupClicked(group)
                    }
                }
            item {
                Box(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun GroupsTopBar() {
    val groupsViewModel: GroupsViewModel = viewModel()
    BigTopBar(
        trailingContent = {
            ProfilePicture(
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp)
                    .clickable {
                        groupsViewModel.onProfilePictureClicked()
                    },
                person = groupsViewModel.requireLoggedInUser
            )
        }
    )
}

@Composable
private fun EmptyGroupList(onAddGroup: () -> Unit) {
    Center(
        modifier = Modifier.padding(16.dp),
    ) {

    }
}