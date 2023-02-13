package com.mikkelthygesen.billsplit.features.main.add_service

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.features.main.add_service.views.SelectParticipantsView
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.isFloat
import com.mikkelthygesen.billsplit.parseToFloat
import com.mikkelthygesen.billsplit.ui.shadowModifier
import com.mikkelthygesen.billsplit.ui.theme.listItemColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceView() {

    val addServiceViewModel: AddServiceViewModel = viewModel()
    val service: SubscriptionService = addServiceViewModel.service
    var monthlyExpense by rememberSaveable {
        if (service.monthlyExpenseState == 0f)
            mutableStateOf("")
        else mutableStateOf(service.monthlyExpenseState.fmt2dec())
    }
    val monthlyPerUser = service.monthlyExpenseState / service.participantsState.size
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadowModifier(listItemColor()),
            value = service.nameState,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true,
            placeholder = {
                Text(text = "Netflix, Disney+", style = TextStyle(fontStyle = FontStyle.Italic))
            },
            onValueChange = {
                service.nameState = it
            })
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .shadowModifier(listItemColor()),
            placeholder = {
                Text(text = "Monthly expense", style = TextStyle(fontStyle = FontStyle.Italic))
            },
            value = monthlyExpense,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_money),
                    contentDescription = "",
                    tint = if (!monthlyExpense.isFloat() && monthlyExpense.isNotBlank())
                        MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            },
            isError = !monthlyExpense.isFloat() && monthlyExpense.isNotBlank(),
            onValueChange = { value ->
                monthlyExpense = value
                service.monthlyExpenseState = value.parseToFloat()
            })
        // Split
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .shadowModifier(listItemColor()),
            text = "Participants will pay $${monthlyPerUser.fmt2dec()} every month"
        )
        // Select Participants
        SelectParticipantsView(subscriptionService = service)
        // Submit
        Box(modifier = Modifier.height(80.dp))
    }
}
