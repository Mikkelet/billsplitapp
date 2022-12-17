package com.mikkelthygesen.billsplit.ui.features.main.groups

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.widgets.GroupListItem
import com.mikkelthygesen.billsplit.ui.widgets.Center
import com.mikkelthygesen.billsplit.ui.widgets.PullToRefreshComposable
import com.mikkelthygesen.billsplit.ui.widgets.RequireUserView

@Composable
fun GroupsList(viewModel: MainViewModel = viewModel()) {

    PullToRefreshComposable(
        initialCallback = {
            viewModel.getGroups(false)
        },
        onRefresh = {
            viewModel.getGroups(true)
        },
        onError = viewModel::handleError
    ) { groups ->
        if (groups.isEmpty())
            Center(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "You are not part of any groups yet!",
                    style = MaterialTheme.typography.body1
                )
                Button(onClick = viewModel::showAddGroup) {
                    Text(text = "Add a new group!")
                }
            }
        else
            RequireUserView(baseViewModel = viewModel) {
                _GroupsView(
                    user = it,
                    groups = groups,
                    onGroupClick = { viewModel.showGroup(it.id) })
            }
    }
}


@Composable
@SuppressLint("ComposableNaming")
private fun _GroupsView(
    user: Person,
    groups: List<Group>,
    onGroupClick: (Group) -> Unit
) {
    Center {
        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = "Groups",
            style = MaterialTheme.typography.h5
        )
        groups.map { group ->
            Column(modifier = Modifier.clickable {
                onGroupClick(group)
            }) {
                GroupListItem(user, group = group, onGroupClick)
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    _GroupsView(
        user = sampleGroup.createdBy,
        groups = (0..5).map { sampleGroup },
        onGroupClick = {})
}