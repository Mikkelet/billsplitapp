package com.mikkelthygesen.billsplit.features.main.group.add_service.views

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.PersonListItem
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.PersonPicker
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor

@Composable
fun SelectPayerView(
    people: List<Person>,
    subscriptionService: SubscriptionService
) {

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    PersonListItem(
        modifier = Modifier.shadowModifier(MaterialTheme.colors.listItemColor()),
        person = subscriptionService.payerState,
        onSelect = {
            showDialog = true
        }) {}

    if (showDialog)
        Dialog(onDismissRequest = {
            showDialog = false
        }) {
            PersonPicker(people = people, onSelect = {
                subscriptionService.payerState = it
                showDialog = false
            })
        }
}