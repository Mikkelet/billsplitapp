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
import com.mikkelthygesen.billsplit.ui.features.group.add_expense.AddSharedExpense
import com.mikkelthygesen.billsplit.ui.features.group.GroupView
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel
import com.mikkelthygesen.billsplit.ui.features.group.view_expenses.ViewExpenses
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.GenericDialog
import com.mikkelthygesen.billsplit.ui.widgets.IconButton

class MainActivity : ComponentActivity() {

    private val viewModel: GroupViewModel by viewModels()

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (val state = viewModel.uiStateFlow.value) {
                is GroupViewModel.UiState.ShowBudget -> handleOnBackPressed()
                is GroupViewModel.UiState.ShowAddExpense -> {
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
                            is GroupViewModel.UiEvent.OnBackPressed -> onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }

                Scaffold(
                    backgroundColor = MaterialTheme.colors.background,
                    topBar = { MainTopBar() },
                    bottomBar = { MainBottomBar(uiState = uiState) }
                ) {
                    Column {
                        when (val dialogState = dialogStateFlow.value) {
                            is GroupViewModel.DialogState.DismissDialogs -> Unit
                            is GroupViewModel.DialogState.ShowAddPersonDialog -> {
                                AddPersonDialog()
                            }
                            is GroupViewModel.DialogState.ShowConfirmEditDialog -> {
                                ConfirmChangesDialog(groupExpense = dialogState.groupExpense)
                            }
                        }
                    }

                    Crossfade(
                        modifier = Modifier.padding(it),
                        targetState = uiState
                    ) { uiState ->
                        when (uiState) {
                            is GroupViewModel.UiState.ShowBudget -> {
                                GroupView()
                            }
                            is GroupViewModel.UiState.ShowAddExpense -> {
                                AddSharedExpense(groupExpense = uiState.sharedExpense)
                            }
                            is GroupViewModel.UiState.ViewExpense -> {
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
    uiState: GroupViewModel.UiState
) {
    when (uiState) {
        is GroupViewModel.UiState.ShowAddExpense -> {
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
                    text = "$${groupExpense.total}",
                    style = TextStyle(color = MaterialTheme.colors.onBackground)
                )
            }
        }
        else -> {}
    }
}

@Composable
private fun MainTopBar(
    viewModel: GroupViewModel = viewModel()
) {
    val uiState = viewModel.uiStateFlow.collectAsState()
    Crossfade(targetState = uiState.value) { state ->
        TopAppBar(
            title = {
                val text = when (state) {
                    is GroupViewModel.UiState.ShowBudget -> "Expenses"
                    is GroupViewModel.UiState.ViewExpense -> "Expenses for ${viewModel.getLoggedIn().nameState}"
                    is GroupViewModel.UiState.ShowAddExpense -> "Add new expense"
                }
                Text(text)
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            actions = {
                when (state) {
                    is GroupViewModel.UiState.ShowBudget -> {
                        IconButton(
                            iconResId = R.drawable.ic_money,
                            onClick = viewModel::showExpenses
                        )
                        IconButton(iconResId = R.drawable.ic_add_plus) {
                            viewModel.showAddPersonDialog()
                        }
                    }
                    is GroupViewModel.UiState.ShowAddExpense ->
                        if (state.sharedExpense.total > 0)
                            IconButton(iconResId = R.drawable.ic_check) {
                                viewModel.saveGroupExpense(state.sharedExpense)
                            }
                    else -> {}
                }
            },
            navigationIcon = {
                if (state !is GroupViewModel.UiState.ShowBudget) {
                    IconButton(iconResId = R.drawable.ic_back) {
                        when (state) {
                            is GroupViewModel.UiState.ShowAddExpense -> {
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
    viewModel: GroupViewModel = viewModel()
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
    viewModel: GroupViewModel = viewModel(),
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
