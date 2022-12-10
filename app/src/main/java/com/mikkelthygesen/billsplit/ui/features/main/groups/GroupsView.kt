package com.mikkelthygesen.billsplit.ui.features.main.groups

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.widgets.GroupListItem
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable

@Composable
fun GroupsList(viewModel: MainViewModel = viewModel()) {
    FutureComposable(asyncCallback = viewModel::getGroups) { groups ->
        LazyColumn {
            items(groups.size) { index ->
                val group = groups[index]
                GroupListItem(onClick = {
                    viewModel.showGroup(groupId = group.id)
                }, group = group)
            }
        }
    }
}