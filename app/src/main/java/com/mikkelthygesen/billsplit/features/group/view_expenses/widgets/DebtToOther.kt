package com.mikkelthygesen.billsplit.features.group.view_expenses.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.ui.widgets.ClickableFutureComposable
import kotlin.math.absoluteValue

@Composable
fun DebtView(
    groupViewModel: GroupViewModel = viewModel(),
    debt: Pair<Person, Float>
) {
    _DebtView(onPay = {
        groupViewModel.addPayment(
            paidTo = debt.first,
            amount = debt.second
        )
    }, onError = groupViewModel::handleError, debt = debt)
}

@Composable
@SuppressLint("ComposableNaming")
private fun _DebtView(
    onPay: suspend () -> Unit,
    onError: (Throwable) -> Unit,
    debt: Pair<Person, Float>
) {
    val isDebt = debt.second > 0
    Row(
        modifier = Modifier.shadowModifier(MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDebt)
            Text(
                modifier = Modifier.weight(2f),
                text = "You owe $${debt.second} to ${debt.first.nameState}",
                style = TextStyle(color = Color.Red, fontSize = 20.sp)
            )
        else
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${debt.first.nameState} owes you $${debt.second.absoluteValue}",
                style = TextStyle(color = Color(0xFF0B9D3A), fontSize = 20.sp)
            )
        if (isDebt)
            ClickableFutureComposable(
                onClickAsync = {
                    onPay()
                },
                onError = onError
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


@Preview
@Composable
private fun PreviewYourDebtView() {
    _DebtView(debt = Pair(Person("ID0", "Mikkel"), 1000F), onError = {}, onPay = {})
}

@Preview
@Composable
private fun PreviewDebt() {
    _DebtView(debt = Pair(Person("ID0", "Mikkel"), -1000F), onError = {}, onPay = {})
}