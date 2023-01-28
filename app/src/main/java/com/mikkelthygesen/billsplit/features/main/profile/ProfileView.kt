package com.mikkelthygesen.billsplit.features.main.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.main.profile.views.ProfileMenuButton
import com.mikkelthygesen.billsplit.features.main.widgets.ProfileHeader

@Composable
@SuppressLint("ComposableNaming")
fun ProfileView(
    user: Person,
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
        ProfileHeader(user, onUpdateUser, onError)
        ProfileMenuButton(text = "Friends") {
            profileViewModel.showFriends()
        }
        ProfileMenuButton(text = "Sign out") {
            profileViewModel.signOut()
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    ProfileView(Person(name = "Catra"), onUpdateUser = {}, onError = {})
}