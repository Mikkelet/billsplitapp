package com.mikkelthygesen.billsplit.ui.features.group.view_expenses

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.interfaces.Event
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun ViewExpenses(
    groupViewModel: GroupViewModel = viewModel(),
    user: Person
) {
    val eventsFlow = groupViewModel.eventStateFlow.collectAsState()
    _ViewExpenses(
        events = eventsFlow.value,
        user = user,
        people = groupViewModel.people
    )
}


@Composable
@SuppressLint("ComposableNaming")
private fun _ViewExpenses(
    events: List<Event>,
    user: Person,
    people: List<Person>
) {
    val payments: List<Payment> = events.filterIsInstance<Payment>()
    val groupExpenses: List<GroupExpense> = events.filterIsInstance<GroupExpense>()

    val calculator = DebtCalculator(people, groupExpenses, payments)
    val debtForPerson = calculator.calculateEffectiveDebtOfPerson(user)
    calculator.logDebt(user)
    Column {
        (debtForPerson)
            .sortedBy { it.second }
            .reversed().map {
                Box(
                    modifier = Modifier.height(64.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (it.second > 0)
                        YourDebt(debt = it, user = user)
                    else if (it.second < 0)
                        DebtToYou(debt = it)
                }
            }
    }
}

@Composable
private fun DebtToYou(debt: Pair<Person, Float>) {
    Text(
        text = "${debt.first.nameState} owes you $${debt.second.absoluteValue}",
        style = TextStyle(color = Color.Green, fontSize = 20.sp)
    )
}

@Composable
private fun YourDebt(
    viewModel: GroupViewModel = viewModel(),
    user: Person,
    debt: Pair<Person, Float>
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(2f),
            text = "You owe $${debt.second} to ${debt.first.nameState}",
            style = TextStyle(color = Color.Red, fontSize = 20.sp)
        )
        if (isLoading)
            CircularProgressIndicator(
                Modifier
                    .weight(1f)
                    .size(16.dp)
                    .wrapContentSize()
            )
        else
            Button(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(),
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        viewModel.addPayment(
                            user = user,
                            paidTo = debt.first,
                            amount = debt.second)
                        isLoading = false
                    }
                }) {
                Text(text = "PAY")
            }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewViewExpense() {
    _ViewExpenses(
        events = sampleSharedExpenses,
        user = samplePeopleShera.first(),
        people = samplePeopleShera
    )
}

@Preview
@Composable
private fun PreviewYourDebtView() {
    YourDebt(debt = Pair(Person("ID0", "Mikkel"), 1000F), user = Person())
}