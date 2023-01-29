package com.mikkelthygesen.billsplit.features.main.groups

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.groups.views.GroupsTitle
import com.mikkelthygesen.billsplit.features.main.widgets.GroupListItem
import com.mikkelthygesen.billsplit.ui.widgets.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupsList(
    uiState: BaseViewModel.UiState,
    user: Person,
) {
    val groupsViewModel: GroupsViewModel = viewModel()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState is BaseViewModel.UiState.Loading,
        onRefresh = { groupsViewModel.getGroups(true) }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshState)
    ) {
        PullRefreshIndicator(
            refreshing = uiState is BaseViewModel.UiState.Loading,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter),
        )
        Crossfade(targetState = uiState) { state ->
            if (state is BaseViewModel.UiState.Loading)
                LoadingView()
            else if (state is GroupsViewModel.ShowGroups)
                LazyColumn {
                    item {
                        GroupsTitle(user = user)
                    }
                    if (state.groups.isEmpty())
                        item {
                            EmptyGroupList {
                                groupsViewModel.addGroup()
                            }
                        }
                    else
                        items(state.groups.size) { index ->
                            val group = state.groups[index]
                            GroupListItem(user, group = group) {
                                groupsViewModel.showGroup(group)
                            }
                        }
                    item {
                        Box(Modifier.height(80.dp))
                    }
                }
        }
    }
}

@Composable
private fun EmptyGroupList(onAddGroup: () -> Unit) {
    Center(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "You are not part of any groups yet!",
            style = MaterialTheme.typography.body1
        )
        Button(onClick = onAddGroup) {
            Text(text = "Add a new group!")
        }
    }
}