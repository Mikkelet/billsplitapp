package com.mikkelthygesen.billsplit.ui.features.main.groups

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.Group
import com.mikkelthygesen.billsplit.sampleGroup
import com.mikkelthygesen.billsplit.ui.features.main.MainViewModel
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.ui.features.main.widgets.GroupListItem
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable

@Composable
fun GroupsList(viewModel: MainViewModel = viewModel()) {
    FutureComposable(
        asyncCallback = viewModel::getGroups,
        onError = viewModel::handleError
    ) { groups ->
        if (groups.isEmpty())
            Text(text = "You are not part of any groups yet!")
        else
            _GroupsView(groups = groups, onGroupClick = { viewModel.showGroup(it.id) })
    }
}


@Composable
@SuppressLint("ComposableNaming")
private fun _GroupsView(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = "Groups",
            style = MaterialTheme.typography.h5
        )
        Column(shadowModifier(MaterialTheme.colors.background)) {
            groups.mapIndexed { index, group ->
                Column(modifier = Modifier.clickable {
                    onGroupClick(group)
                }) {
                    GroupListItem(group = group)
                    if (index != groups.lastIndex)
                        Divider()
                }

            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    _GroupsView(groups = (0..5).map { sampleGroup }, onGroupClick = {})
}