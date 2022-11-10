package com.mikkelthygesen.billsplit

import android.os.Bundle
import android.provider.Telephony.Mms.Part
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.widgets.ParticipantView
import com.mikkelthygesen.billsplit.widgets.SharedExpensesView

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val peopleState = viewModel.peopleState.collectAsState()
            val sharedOwed = viewModel.sharedOwedState.collectAsState()
            BillSplitTheme {
                MainScaffold(onFabClick = viewModel::addPerson) {
                    SharedExpensesView(
                        shared = peopleState.value.first(),
                        onChangeListener = viewModel::updateSharedExpenses
                    )
                    LazyColumn {
                        val peopleStateSnap = peopleState.value.subList(1, peopleState.value.size)
                        items(
                            count = peopleStateSnap.size,
                            key = { peopleStateSnap[it].name }) { index ->
                            val person = peopleStateSnap[index]
                            ParticipantView(
                                person = person,
                                sharedOwed = sharedOwed.value,
                                onChangeListener = {
                                    viewModel.updateOwed(person, it)
                                },
                                onRemoveClicked = {
                                    viewModel.removePerson(person)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainScaffold(onFabClick: () -> Unit, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = onFabClick
                ) {
                    Text(text = "+")
                }
            }
        ) {
            Column {
                content()
            }
        }
    }
}


@Composable
fun DebtOverview(people: List<Person>) {
    val shared = people.first()
    val individuals = people.subList(1, people.size)
    val sharedDivided = when {
        individuals.isEmpty() -> 0F
        shared.owed == 0F -> 0F
        else -> shared.owed / individuals.count()
    }
    val calcDebt = individuals.map {
        val owedTotal = it.owed + sharedDivided
        Person(it.name, owedTotal)
    }
    Column {
        Text(text = "Shared = ${shared.owed} / ${individuals.count()} = $sharedDivided")
        LazyColumn {
            items(calcDebt.size) {
                val person = calcDebt[it]
                Text(text = "${person.name}: ${person.owed}")
            }
        }
    }
}

