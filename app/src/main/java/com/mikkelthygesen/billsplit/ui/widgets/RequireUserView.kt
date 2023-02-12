package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.base.BaseViewModel

@Composable
fun RequireUserView(
    baseViewModel: BaseViewModel,
    content: @Composable (Person) -> Unit
) {
    val userFlow = baseViewModel.loggedInUserFlow.collectAsState()
    val userState = userFlow.value
    if (userState != null) {
        content(baseViewModel.requireLoggedInUser)
    } else LoadingView()
}