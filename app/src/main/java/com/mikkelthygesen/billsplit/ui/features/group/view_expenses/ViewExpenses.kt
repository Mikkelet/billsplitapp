package com.mikkelthygesen.billsplit.ui.features.group.view_expenses

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.models.Payment
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel
import kotlin.math.absoluteValue

@Composable
fun ViewExpenses(
    viewModel: GroupViewModel = viewModel()
) {
    val paymentsFlow = viewModel.paymentsStateFlow.collectAsState()
    val groupExpensesFlow = viewModel.sharedExpensesStateFlow.collectAsState()
    val user = viewModel.getLoggedIn()
    val paymentsState = paymentsFlow.value
    val groupExpenses = groupExpensesFlow.value

    val calculator = DebtCalculator(viewModel.people, groupExpenses, paymentsState)
    val debtForPerson = calculator.calculateEffectiveDebtOfPerson(user)
    calculator.logDebt(user)
    (debtForPerson)
        .sortedBy { it.second }
        .reversed().map {
            Box(
                modifier = Modifier.height(64.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (it.second > 0)
                    YourDebt(debt = it)
                else if (it.second < 0)
                    DebtToYou(debt = it)
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
    debt: Pair<Person, Float>
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(2f),
            text = "You owe $${debt.second} to ${debt.first.nameState}",
            style = TextStyle(color = Color.Red, fontSize = 20.sp)
        )
        Button(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(),
            onClick = {
                val payment = Payment(viewModel.getLoggedIn(), debt.first, debt.second)
                viewModel.addPayment(payment)
            }) {
            Text(text = "PAY")
        }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewViewExpense() {
    ViewExpenses()
}

@Preview
@Composable
private fun PreviewYourDebtView() {
    YourDebt(debt = Pair(Person("ID0", "Mikkel"), 1000F))
}