package com.mikkelthygesen.billsplit.features.main.add_service

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.features.main.add_service.views.SelectParticipantsView
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.isFloat
import com.mikkelthygesen.billsplit.parseToFloat
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton

@Composable
fun AddServiceView() {

    val addServiceViewModel: AddServiceViewModel = viewModel()
    val service: SubscriptionService = addServiceViewModel.service
    var monthlyExpense by rememberSaveable {
        mutableStateOf("${service.monthlyExpenseState}")
    }
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        BigTopBar(
            leadingContent = {
                BackButton {
                    addServiceViewModel.onBackButtonPressed()
                }
            },
            trailingContent = {
                SimpleIconButton(iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_check) {
                    addServiceViewModel.addSubscriptionService()
                }
            }
        )
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
        Box(modifier = Modifier.height(80.dp))
    }
}
