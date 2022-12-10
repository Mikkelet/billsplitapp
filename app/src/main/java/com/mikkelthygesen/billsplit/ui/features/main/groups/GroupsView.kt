package com.mikkelthygesen.billsplit.ui.features.main.groups

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.widgets.GroupListItem
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import kotlinx.coroutines.launch

@Composable
fun GroupsList(viewModel: MainViewModel = viewModel()) {
    var loadingState by remember {
        mutableStateOf(false)
    }
    var groupsState by remember {
        mutableStateOf(emptyList<Group>())
    }
    LaunchedEffect(Unit) {
        loadingState = true
        val groups = kotlin.runCatching { viewModel.getGroups() }
        groups.fold(
            onSuccess = {
                groupsState = it
                loadingState = false
            },
            onFailure = {}
        )
    }

    if (loadingState)
        LoadingView()
    else
        LazyColumn {
            items(groupsState.size) { index ->
                val group = groupsState[index]
                GroupListItem(onClick = {
                    viewModel.showGroup(groupId = group.id)
                }, group = group)
            }
        }
}