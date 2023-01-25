package com.mikkelthygesen.billsplit.features.group

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.group.add_expense.ExpenseView
import com.mikkelthygesen.billsplit.features.group.group_view.GroupEventsView
import com.mikkelthygesen.billsplit.features.group.view_expenses.ViewDebt
import com.mikkelthygesen.billsplit.features.group.widgets.ConfirmChangesDialog
import com.mikkelthygesen.billsplit.features.main.profile.widget.shadowModifier
import com.mikkelthygesen.billsplit.features.main.signup.SignInView
import com.mikkelthygesen.billsplit.features.main.signup.SignUpView
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.ErrorDialog
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupActivity : ComponentActivity() {

    private val viewModel: GroupViewModel by viewModels()
    private val groupId by lazy { intent.extras?.getString("group_id") ?: "" }

    init {
        onBackPressedDispatcher.addCallback(this) {
            when (val state = viewModel.uiStateFlow.value) {
                is GroupViewModel.EditExpense -> {
                    if (state.groupExpense.isChanged())
                        viewModel.showConfirmChangesDialog(state.groupExpense)
                    else viewModel.showChat()
                }
                is GroupViewModel.ShowDebt -> viewModel.showChat()
                else -> finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getGroup(groupId)

        setContent {
            BillSplitTheme {
                val groupUiState = viewModel.uiStateFlow.collectAsState()
                val uiState = groupUiState.value

                when (val state = viewModel.dialogState) {
                    is GroupViewModel.ConfirmChangesDialog -> ConfirmChangesDialog(
                        groupExpense = state.groupExpense
                    )
                    is BaseViewModel.DialogState.Error -> ErrorDialog(
                        exception = state.exception,
                        onDismiss = viewModel::dismissDialog
                    )
                    is BaseViewModel.DialogState.DismissDialogs -> Unit
                }

                LaunchedEffect(Unit) {
                    viewModel.uiEventsState.collect { event ->
                        when (event) {
                            is BaseViewModel.UiEvent.OnBackPressed -> onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }

                Scaffold(
                    bottomBar = { BottomBar(uiState) }
                ) {
                    Box {
                        Crossfade(targetState = uiState, modifier = Modifier.padding(it)) { state ->
                            when (state) {
                                is BaseViewModel.UiState.Loading -> LoadingView()
                                is GroupViewModel.Chat -> GroupEventsView()
                                is GroupViewModel.ShowDebt -> ViewDebt()
                                is GroupViewModel.EditExpense -> ExpenseView(groupExpense = state.groupExpense)
                            }
                        }
                        if (viewModel.showChatLoader)
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.background)
                                    .padding(4.dp)
                                    .align(Alignment.TopCenter)
                            )
                        TopBar(
                            modifier = Modifier.align(Alignment.TopCenter)
                        ) {
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    groupViewModel: GroupViewModel = viewModel(),
    onBackPressed: () -> Unit
) {
    val uiState = groupViewModel.uiStateFlow.collectAsState()

    Crossfade(targetState = uiState.value) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                BackButton(onClick = onBackPressed)
                val title = when (it) {
                    GroupViewModel.Chat -> groupViewModel.group.nameState
                    GroupViewModel.ShowDebt -> "Debts"
                    is GroupViewModel.EditExpense -> "Expense"
                    else -> ""
                }
                Text(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.background)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    text = title,
                    style = MaterialTheme.typography.h6
                )
            }
            Row(
                Modifier.shadowModifier(
                    MaterialTheme.colors.background,
                    innerPadding = PaddingValues(0.dp),
                    cornerShape = RoundedCornerShape(90),
                    outerPadding = PaddingValues(0.dp)
                )
            ) {
                if (it is GroupViewModel.EditExpense)
                    SimpleIconButton(iconResId = R.drawable.ic_check) {
                        groupViewModel.saveGroupExpense(it.groupExpense)
                    }
                if (it is GroupViewModel.Chat)
                    SimpleIconButton(iconResId = R.drawable.ic_baseline_settings_24) {
                        // show settings
                    }
                if (it is GroupViewModel.Chat)
                    SimpleIconButton(
                        iconResId = R.drawable.ic_money,
                        onClick = groupViewModel::showDebt
                    )
            }
        }
    }
}

@Composable
private fun BottomBar(
    uiState: BaseViewModel.UiState
) {
    when (uiState) {
        is GroupViewModel.EditExpense -> {
            val groupExpense = uiState.groupExpense
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
    }
}