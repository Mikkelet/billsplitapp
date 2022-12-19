package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.base.BaseViewModel
import com.mikkelthygesen.billsplit.data.auth.NetworkExceptions
import com.mikkelthygesen.billsplit.models.Person

@Composable
fun RequireUserView(
    baseViewModel: BaseViewModel,
    content: @Composable (Person) -> Unit
) {
    if (baseViewModel.loggedInUser != null)
        content(baseViewModel.loggedInUser)
    else baseViewModel.handleError(NetworkExceptions.UserLoggedOutException)
}