package com.mikkelthygesen.billsplit.ui.widgets

import androidx.compose.runtime.Composable
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.base.BaseViewModel

@Composable
fun RequireUserView(
    baseViewModel: BaseViewModel,
    content: @Composable (Person) -> Unit
) {
    if (baseViewModel.loggedIdUser != null) {
        content(baseViewModel.requireLoggedInUser)
    } else SignedOutWarning {
        baseViewModel.showLanding()
    }
}