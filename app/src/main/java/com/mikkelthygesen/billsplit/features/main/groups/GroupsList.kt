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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Group
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.features.main.widgets.GroupListItem
import com.mikkelthygesen.billsplit.ui.widgets.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupsList(
    uiState: BaseViewModel.UiState,
    user: Person,
    onAddGroup: () -> Unit,
    showGroup: (Group) -> Unit,
    getGroups: () -> Unit,
    onRefresh: () -> Unit
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState is BaseViewModel.UiState.Loading,
        onRefresh = onRefresh)

    LaunchedEffect(Unit) {
        getGroups()
    }

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
                        Text(
                            modifier = Modifier.padding(
                                top = 32.dp,
                                start = 32.dp,
                                bottom = 32.dp
                            ),
                            text = "Groups",
                            style = MaterialTheme.typography.h4
                        )
                    }
                    if (state.groups.isEmpty())
                        item {
                            EmptyGroupList(onAddGroup)
                        }
                    else
                        items(state.groups.size) { index ->
                            val group = state.groups[index]
                            GroupListItem(user, group = group) {
                                showGroup(group)
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