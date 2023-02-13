package com.mikkelthygesen.billsplit.features.main.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.profile.views.ProfileMenuButton
import com.mikkelthygesen.billsplit.features.main.profile.views.SignOutButton
import com.mikkelthygesen.billsplit.features.main.profile.views.ProfileHeader

@Composable
@SuppressLint("ComposableNaming")
fun ProfileView(
    onUpdateUser: suspend () -> Unit,
    onError: (Throwable) -> Unit
) {
    val profileViewModel: ProfileViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(profileViewModel.requireLoggedInUser, onUpdateUser, onError)
        ProfileMenuButton(text = "Friends") {
            profileViewModel.showFriends()
        }
        Divider(modifier = Modifier.padding(32.dp))
        SignOutButton()
        Box(modifier = Modifier.height(80.dp))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    ProfileView(onUpdateUser = {}, onError = {})
}