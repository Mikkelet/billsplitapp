package com.mikkelthygesen.billsplit.features.main.profile.views

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.profile.ProfileViewModel
import com.mikkelthygesen.billsplit.ui.widgets.GenericDialog

@Composable
fun SignOutButton() {
    val profileViewModel: ProfileViewModel = viewModel()

    var showConfirmDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (showConfirmDialog) {
        GenericDialog(
            dialogText = "Are you sure you want to sign out?",
            primaryText = "Yes",
            onDismiss = { showConfirmDialog = false },
            primaryAction = {
                showConfirmDialog = false
                profileViewModel.signOut()
            },
            secondaryText = "No",
            secondaryAction = {
                showConfirmDialog = false
            }
        )
    }

    ProfileMenuButton(
        text = "Sign out",
        style = TextStyle(color = MaterialTheme.colors.error)
    ) {
        showConfirmDialog = true
    }
}