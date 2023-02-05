package com.mikkelthygesen.billsplit.features.main.group.add_service

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.group.add_service.views.SelectParticipantsView
import com.mikkelthygesen.billsplit.isFloat
import com.mikkelthygesen.billsplit.parseToFloat
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView

@Composable
fun AddServiceView(service: SubscriptionService) {

    val groupViewModel: GroupViewModel = viewModel()
    var monthlyExpense by rememberSaveable {
        mutableStateOf("${service.monthlyExpenseState}")
    }
    var loading by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit, block = {
        groupViewModel.uiEventsState.collect { event ->
            when (event) {
                is GroupViewModel.SaveServiceClicked -> {
                    groupViewModel.addSubscriptionService(service)
                    loading = true
                }
                is GroupViewModel.SaveServiceFailed -> {
                    loading = false
                }
                is GroupViewModel.ServiceSaved -> {
                    groupViewModel.onBackButtonPressed()
                }
            }
        }
    })

    if (loading)
        LoadingView()
    else
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(32.dp),
                text = "Add new Service",
                style = MaterialTheme.typography.h4
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadowModifier(MaterialTheme.colors.listItemColor()),
                value = service.nameState,
                placeholder = {
                    Text(text = "Netflix, Disney+", style = TextStyle(fontStyle = FontStyle.Italic))
                },
                onValueChange = {
                    service.nameState = it
                })
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadowModifier(MaterialTheme.colors.listItemColor()),
                placeholder = {
                    Text(text = "Monthly expense", style = TextStyle(fontStyle = FontStyle.Italic))
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = com.mikkelthygesen.billsplit.R.drawable.ic_money),
                        contentDescription = ""
                    )
                },
                value = monthlyExpense,
                isError = !monthlyExpense.isFloat() && monthlyExpense.isNotBlank(),
                onValueChange = { value ->
                    monthlyExpense = value
                    service.monthlyExpenseState = value.parseToFloat()
                })
            // Split
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadowModifier(MaterialTheme.colors.listItemColor()),
                text = "Participants will pay $${service.monthlyExpenseState / service.participantsState.size} every month"
            )
            // Select Participants
            SelectParticipantsView(subscriptionService = service)
            // Submit
        }
}
