package com.mikkelthygesen.billsplit.features.main.group.add_service.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.PersonListItem
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.PersonPicker
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import com.mikkelthygesen.billsplit.ui.widgets.FlatButton

@Composable
fun SelectParticipantsView(subscriptionService: SubscriptionService) {

    val groupViewModel: GroupViewModel = viewModel()
    val group = groupViewModel.group
    var showPeoplePicker by rememberSaveable {
        mutableStateOf(false)
    }

    if (showPeoplePicker)
        Dialog(onDismissRequest = { showPeoplePicker = false }) {
            PersonPicker(people = group.peopleState.minus(subscriptionService.participantsState.toSet()),
                onSelect = { person ->
                    subscriptionService.addParticipant(person)
                    showPeoplePicker = false
                })
        }
    Column(
        Modifier
            .animateContentSize()
            .shadowModifier(
                MaterialTheme.colors
                    .listItemColor()
            )
    ) {
        val showAddButton = subscriptionService.participantsState.size < groupViewModel.people.size
        subscriptionService.participantsState.map { person ->
            val text =
                if (subscriptionService.payerState == person) "${person.nameState} is paying" else person.nameState
            val showRemoveButton = person != groupViewModel.requireLoggedInUser &&
                    person != subscriptionService.payerState
            val showPayerIcon = person == subscriptionService.payerState
            PersonListItem(
                person = person,
                text = text,
                onSelect = {
                    subscriptionService.payerState = it
                },
                trailingContent = {
                    if (showRemoveButton)
                        IconButton(
                            onClick = {
                                subscriptionService.removeParticipant(person)
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                                tint = MaterialTheme.colors.error,
                                contentDescription = "Remove participant"
                            )
                        }
                    else if (showPayerIcon)
                        Icon(
                            modifier = Modifier.padding(12.dp),
                            painter = painterResource(id = R.drawable.ic_money),
                            contentDescription = "Payer"
                        )
                })
        }
        if (showAddButton)
            FlatButton(
                text = "Add",
                modifier = Modifier.align(Alignment.End)
            ) {
                showPeoplePicker = true
            }
    }
}

