package com.mikkelthygesen.billsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.ui.features.main.AddSharedExpense
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetView
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetViewModel
import com.mikkelthygesen.billsplit.ui.features.view_expenses.ViewExpenses
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.DescriptionTextField
import com.mikkelthygesen.billsplit.ui.widgets.FabView
import com.mikkelthygesen.billsplit.ui.widgets.IconButton

class MainActivity : ComponentActivity() {

    private val viewModel = SharedBudgetViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState = viewModel.uiStateFlow.collectAsState()
            val sharedExpenses = viewModel.sharedExpensesState.collectAsState()
            var showAddPersonDialog by remember { mutableStateOf(false) }
            BillSplitTheme {
                val state = uiState.value
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                val text = when (state) {
                                    is SharedBudgetViewModel.UiState.ShowBudget -> "Expenses"
                                    is SharedBudgetViewModel.UiState.ViewExpense -> "Expenses for ${viewModel.getLoggedIn().name}"
                                    is SharedBudgetViewModel.UiState.ShowAddExpense -> "Add new expense"
                                }
                                Text(text)
                            },
                            actions = {
                                when (state) {
                                    is SharedBudgetViewModel.UiState.ShowBudget -> {
                                        IconButton(R.drawable.ic_money, viewModel::showExpenses)
                                        IconButton(R.drawable.ic_add_plus) {
                                            showAddPersonDialog = true
                                        }
                                    }
                                    is SharedBudgetViewModel.UiState.ShowAddExpense ->
                                        if (state.sharedExpense.getTotal() > 0)
                                            IconButton(R.drawable.ic_check) {
                                                viewModel.onAddSharedExpense(state.sharedExpense)
                                            }
                                    else -> {}
                                }
                            },
                            navigationIcon = {
                                if (state !is SharedBudgetViewModel.UiState.ShowBudget) {
                                    IconButton(R.drawable.ic_back, this::onBackPressed)
                                }
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        Crossfade(targetState = state) {
                            when (it) {
                                is SharedBudgetViewModel.UiState.ShowBudget ->
                                    FabView(onClick = viewModel::addExpense)
                                else -> {}
                            }
                        }
                    },
                    bottomBar = {
                        if (state is SharedBudgetViewModel.UiState.ShowAddExpense) {
                            val groupExpense = state.sharedExpense
                            DescriptionTextField(initialValue = groupExpense.description) {
                                groupExpense.description = it
                            }
                        }
                    }
                ) {
                    Column {
                        if (showAddPersonDialog) AddPersonDialog(
                            onConfirm = {
                                viewModel.addPerson(it)
                                showAddPersonDialog = false
                            },
                            onDismiss = { showAddPersonDialog = false })
                        Crossfade(targetState = state) {
                            when (it) {
                                is SharedBudgetViewModel.UiState.ShowBudget -> {
                                    SharedBudgetView(
                                        sharedExpenses = sharedExpenses.value,
                                        onSharedExpenseClicked = viewModel::editSharedExpense
                                    )
                                }
                                is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                                    AddSharedExpense(
                                        groupExpense = it.sharedExpense,
                                        addSharedExpenseCallback = viewModel
                                    )
                                }
                                is SharedBudgetViewModel.UiState.ViewExpense -> {
                                    ViewExpenses(
                                        person = viewModel.getLoggedIn(),
                                        people = it.people.map { ie -> ie.person },
                                        expenses = sharedExpenses.value
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun slideUp(density: Density) = slideInVertically {
        with(density) {
            +50.dp.roundToPx()
        }
    }

    private fun slideDown(density: Density) = slideOutVertically {
        with(density) {
            -50.dp.roundToPx()
        }
    }

    override fun onBackPressed() {
        if (viewModel.uiStateFlow.value !is SharedBudgetViewModel.UiState.ShowBudget) {
            viewModel.showBudget()
        }
    }
}

@Composable
private fun AddPersonDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var nameTextFieldValue by remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(text = "Enter new name") },
        text = {
            TextField(value = nameTextFieldValue,
                onValueChange = { nameTextFieldValue = it },
                isError = nameTextFieldValue.isBlank(),
                placeholder = {
                    Text(text = "Enter a name")
                },
                trailingIcon = {
                    IconButton(onClick = { nameTextFieldValue = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_cancel_24),
                            contentDescription = "Clear text"
                        )
                    }
                })
        },
        confirmButton = {
            Button(onClick = {
                if (nameTextFieldValue.isNotBlank()) onConfirm(nameTextFieldValue)
            }) {
                Text(text = "Add")
            }
        })
}

