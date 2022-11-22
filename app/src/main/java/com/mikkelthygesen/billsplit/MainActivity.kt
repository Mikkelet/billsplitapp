package com.mikkelthygesen.billsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.ui.features.add_expense.AddSharedExpense
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetView
import com.mikkelthygesen.billsplit.ui.features.shared_budget.SharedBudgetViewModel
import com.mikkelthygesen.billsplit.ui.features.view_expenses.ViewExpenses
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.FabView
import com.mikkelthygesen.billsplit.ui.widgets.GenericDialog
import com.mikkelthygesen.billsplit.ui.widgets.IconButton

class MainActivity : ComponentActivity() {
    private val viewModel: SharedBudgetViewModel by viewModels()

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (val state = viewModel.uiStateFlow.value) {
                is SharedBudgetViewModel.UiState.ShowBudget -> handleOnBackPressed()
                is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                    if (state.sharedExpense.isChanged())
                        viewModel.showConfirmChangesDialog(state.sharedExpense)
                    else viewModel.showBudget()
                }
                else -> viewModel.showBudget()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiStateFlow = viewModel.uiStateFlow.collectAsState()
            val dialogStateFlow = viewModel.dialogState.collectAsState()

            BillSplitTheme {
                val uiState = uiStateFlow.value

                LaunchedEffect(Unit) {
                    viewModel.uiEventsState.collect { uiEvent ->
                        when (uiEvent) {
                            is SharedBudgetViewModel.UiEvent.OnBackPressed -> onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }

                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    topBar = { MainTopBar() },
                    floatingActionButtonPosition = FabPosition.End,
                    floatingActionButton = {
                        Crossfade(targetState = uiState) {
                            when (it) {
                                is SharedBudgetViewModel.UiState.ShowBudget ->
                                    FabView()
                                else -> {}
                            }
                        }
                    },
                    bottomBar = { MainBottomBar(uiState) }
                ) {
                    Column {
                        when (val dialogState = dialogStateFlow.value) {
                            is SharedBudgetViewModel.DialogState.DismissDialogs -> Unit
                            is SharedBudgetViewModel.DialogState.ShowAddPersonDialog -> {
                                AddPersonDialog()
                            }
                            is SharedBudgetViewModel.DialogState.ShowConfirmEditDialog -> {
                                ConfirmChangesDialog(groupExpense = dialogState.groupExpense)
                            }
                        }
                    }

                    Crossfade(
                        modifier = Modifier.padding(it),
                        targetState = uiState
                    ) { uiState ->
                        when (uiState) {
                            is SharedBudgetViewModel.UiState.ShowBudget -> {
                                SharedBudgetView()
                            }
                            is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                                AddSharedExpense(groupExpense = uiState.sharedExpense)
                            }
                            is SharedBudgetViewModel.UiState.ViewExpense -> {
                                ViewExpenses()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    uiState: SharedBudgetViewModel.UiState
) {
    if (uiState is SharedBudgetViewModel.UiState.ShowAddExpense) {
        val groupExpense = uiState.sharedExpense
        Row(
            Modifier
                .background(MaterialTheme.colors.background)
                .padding(8.dp),
            Arrangement.Center
        ) {
            Text(
                text = "Total",
                style = TextStyle(color = MaterialTheme.colors.onBackground)
            )
            Box(Modifier.weight(1f))
            Text(
                text = "$${groupExpense.getTotal()}",
                style = TextStyle(color = MaterialTheme.colors.onBackground)
            )
        }
    }
}

@Composable
private fun MainTopBar(
    viewModel: SharedBudgetViewModel = viewModel()
) {
    val uiState = viewModel.uiStateFlow.collectAsState()
    Crossfade(targetState = uiState.value) { state ->
        TopAppBar(
            title = {
                val text = when (state) {
                    is SharedBudgetViewModel.UiState.ShowBudget -> "Expenses"
                    is SharedBudgetViewModel.UiState.ViewExpense -> "Expenses for ${viewModel.getLoggedIn().nameState}"
                    is SharedBudgetViewModel.UiState.ShowAddExpense -> "Add new expense"
                }
                Text(text)
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            actions = {
                when (state) {
                    is SharedBudgetViewModel.UiState.ShowBudget -> {
                        IconButton(
                            iconResId = R.drawable.ic_money,
                            onClick = viewModel::showExpenses
                        )
                        IconButton(iconResId = R.drawable.ic_add_plus) {
                            viewModel.showAddPersonDialog()
                        }
                    }
                    is SharedBudgetViewModel.UiState.ShowAddExpense ->
                        if (state.sharedExpense.getTotal() > 0)
                            IconButton(iconResId = R.drawable.ic_check) {
                                viewModel.saveGroupExpense(state.sharedExpense)
                            }
                    else -> {}
                }
            },
            navigationIcon = {
                if (state !is SharedBudgetViewModel.UiState.ShowBudget) {
                    IconButton(iconResId = R.drawable.ic_back) {
                        when (state) {
                            is SharedBudgetViewModel.UiState.ShowAddExpense -> {
                                val groupExpense = state.sharedExpense
                                if (!groupExpense.isChanged())
                                    viewModel.onBackButtonPressed()
                                else {
                                    viewModel.showConfirmChangesDialog(state.sharedExpense)
                                }
                            }
                            else -> viewModel.onBackButtonPressed()
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun AddPersonDialog(
    viewModel: SharedBudgetViewModel = viewModel()
) {
    var nameTextFieldValue by remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = viewModel::dismissDialogs,
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
                if (nameTextFieldValue.isNotBlank()) {
                    viewModel.addPerson(nameTextFieldValue)
                    viewModel.dismissDialogs()
                }
            }) {
                Text(text = "Add")
            }
        })
}


@Composable
private fun ConfirmChangesDialog(
    viewModel: SharedBudgetViewModel = viewModel(),
    groupExpense: GroupExpense
) {
    GenericDialog(
        dialogText = "You made changes to the expense. Keep editing or revert changes?",
        primaryText = "Keep editing",
        primaryAction = viewModel::dismissDialogs,
        secondaryText = "Revert changes and cancel",
        secondaryAction = {
            groupExpense.revertChanges()
            viewModel.dismissDialogs()
            viewModel.onBackButtonPressed()
        }
    )
}
