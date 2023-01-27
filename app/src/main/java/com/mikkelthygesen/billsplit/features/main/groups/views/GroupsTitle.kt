package com.mikkelthygesen.billsplit.features.main.groups.views


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.main.groups.GroupsViewModel
import com.mikkelthygesen.billsplit.ui.widgets.ProfilePicture

@Composable
fun GroupsTitle(user: Person) {
    val groupsViewModel: GroupsViewModel = viewModel()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Splitsby",
            style = MaterialTheme.typography.h4
        )
        ProfilePicture(
            modifier = Modifier
                .size(64.dp)
                .clickable {
                    groupsViewModel.onProfilePictureClicked()
                }, person = user
        )
    }
}