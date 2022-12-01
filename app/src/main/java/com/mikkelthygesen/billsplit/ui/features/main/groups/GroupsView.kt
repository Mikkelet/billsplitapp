package com.mikkelthygesen.billsplit.ui.features.main.groups

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.widgets.GroupListItem

@Composable
fun GroupsList(viewModel: MainViewModel = viewModel(), groups: List<Group>) {
    LazyColumn {
        items(groups.size) { index ->
            val group = groups[index]
            GroupListItem(onClick = {
                viewModel.showGroup(groupId = group.id)
            }, group = group)
        }
    }
}