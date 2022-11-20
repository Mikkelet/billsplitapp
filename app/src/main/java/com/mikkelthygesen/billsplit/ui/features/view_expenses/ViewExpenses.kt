package com.mikkelthygesen.billsplit.ui.features.view_expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.mikkelthygesen.billsplit.DebtCalculator
import com.mikkelthygesen.billsplit.models.Person
import com.mikkelthygesen.billsplit.models.GroupExpense
import kotlin.math.absoluteValue

@Composable
fun ViewExpenses(
    person: Person,
    people: List<Person>,
    expenses: List<GroupExpense>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Expenses overview for ${person.nameState}")
        val calculator = DebtCalculator(people, expenses)
        val debtForPerson = calculator.calculateEffectiveDebtOfPerson(person)
        calculator.logDebt(person)
        debtForPerson.map {
            Column {
                if (it.second > 0)
                    Text(
                        text = "You owe $${it.second} to ${it.first.nameState}",
                        style = TextStyle(color = Color.Red, fontSize = 30.sp)
                    )
                else if (it.second < 0)
                    Text(
                        text = "${it.first.nameState} owes you $${it.second.absoluteValue}",
                        style = TextStyle(color = Color.Green, fontSize = 30.sp)
                    )
            }
        }
    }
}