package com.mikkelthygesen.billsplit

import android.os.Bundle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.widgets.ParticipantView
import com.mikkelthygesen.billsplit.widgets.SharedExpensesView

class MainActivity : ComponentActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val peopleState = viewModel.participantsState.collectAsState()
            BillSplitTheme {
                MainScaffold(onFabClick = viewModel::addPerson) {
                    LazyColumn {
                        val peopleStateSnap = peopleState.value
                        items(
                            count = peopleStateSnap.size,
                            key = { peopleStateSnap[it].name }
                        ) { index ->
                            val participant = peopleStateSnap[index]
                            if (index == 0) // Shared is also first participant
                                SharedExpensesView(
                                    shared = participant,
                                    onChangeListener = viewModel::updateSharedExpenses
                                )
                            else ParticipantView(
                                person = participant,
                                sharedOwed = viewModel.getSharedExpenses().owed / peopleStateSnap.size,
                                onChangeListener = {
                                    viewModel.updateOwed(participant, it)
                                },
                                onRemoveClicked = {
                                    viewModel.removePerson(participant)
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
private fun MainScaffold(
    onFabClick: () -> Unit,
    content: @Composable () -> Unit
) {
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
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_plus),
                        contentDescription = "Add participant"
                    )
                }
            }
        ) {
            Column {
                content()
            }
        }
    }
}

