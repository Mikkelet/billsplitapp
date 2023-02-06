package com.mikkelthygesen.billsplit.features.main.group.view_expenses

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.group.view_expenses.widgets.DebtView
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.domain.models.interfaces.Event
import com.mikkelthygesen.billsplit.samplePeopleShera
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.ui.widgets.RequireUserView

@Composable
fun ViewDebt(
    groupViewModel: GroupViewModel = viewModel(),
) {
    val eventsFlow = groupViewModel.eventStateFlow.collectAsState()
    RequireUserView(baseViewModel = groupViewModel) {
        _ViewDebt(
            events = eventsFlow.value,
            user = it,
            people = groupViewModel.people
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun TestPreview(){
    Test(user = Person())
}

@Composable
fun Test(user: Person, viewModel:GroupViewModel = hiltViewModel()){
    Text(text = "hello world")
}


@Composable
@SuppressLint("ComposableNaming")
private fun _ViewDebt(
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
        Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
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
                    if (it.second != 0F)
                        DebtView(debt = it)
                }
            }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewViewExpense() {
    _ViewDebt(
        events = sampleSharedExpenses,
        user = samplePeopleShera.first(),
        people = samplePeopleShera
    )
}

