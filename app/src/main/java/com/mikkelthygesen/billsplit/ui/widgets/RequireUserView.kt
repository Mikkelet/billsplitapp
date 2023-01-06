package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.data.remote.exceptions.NetworkExceptions
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.domain.models.Person

@Composable
fun RequireUserView(
    baseViewModel: BaseViewModel,
    content: @Composable (Person) -> Unit
) {
    if (baseViewModel.loggedIdUser != null) {
        content(baseViewModel.requireLoggedInUser)
    } else baseViewModel.handleError(NetworkExceptions.UserLoggedOutException)
}