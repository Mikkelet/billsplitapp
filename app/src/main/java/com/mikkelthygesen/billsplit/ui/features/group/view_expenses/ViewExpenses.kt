package com.mikkelthygesen.billsplit.ui.features.group.view_expenses

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import com.mikkelthygesen.billsplit.ui.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.ui.widgets.ClickableFutureComposable
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
    Column(
        Modifier.fillMaxWidth().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
            text = "Debts",
            style = MaterialTheme.typography.h5
        )
        (debtForPerson)
            .sortedBy { it.second }
            .reversed().map {
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (it.second > 0)
                        YourDebt(debt = it)
                    else if (it.second < 0)
                        DebtToYou(debt = it)
                }
            }
    }
}

@Composable
private fun DebtToYou(debt: Pair<Person, Float>) {
    Text(
        modifier = Modifier.fillMaxWidth().shadowModifier(MaterialTheme.colors.background),
        text = "${debt.first.nameState} owes you $${debt.second.absoluteValue}",
        style = TextStyle(color = Color(0xFF0B9D3A), fontSize = 20.sp)
    )
}

@Composable
private fun YourDebt(
    viewModel: GroupViewModel = viewModel(),
    debt: Pair<Person, Float>
) {
    Row(
        modifier = Modifier.shadowModifier(MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(2f),
            text = "You owe $${debt.second} to ${debt.first.nameState}",
            style = TextStyle(color = Color.Red, fontSize = 20.sp)
        )
        ClickableFutureComposable(
            onClickAsync = {
                viewModel.addPayment(
                    paidTo = debt.first,
                    amount = debt.second
                )
            },
            onError = viewModel::handleError
        ) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(),
                onClick = it
            ) {
                Text(text = "PAY")
            }
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
    YourDebt(debt = Pair(Person("ID0", "Mikkel"), 1000F))
}