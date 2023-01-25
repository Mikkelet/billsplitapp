package com.mikkelthygesen.billsplit.features.main.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.features.main.profile.widget.FriendsListView
import com.mikkelthygesen.billsplit.features.main.widgets.ProfileHeader
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.RequireUserView

@Composable
@SuppressLint("ComposableNaming")
fun ProfileView(
    user: Person,
    onUpdateUser: suspend () -> Unit,
    onError: (Throwable) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(user, onUpdateUser, onError)
        FriendsListView()
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    ProfileView(Person(name = "Catra"), onUpdateUser = {}, onError = {})
}