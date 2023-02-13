package com.mikkelthygesen.billsplit.features.main.add_expense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.add_expense.views.ExpenseViewHeader
import com.mikkelthygesen.billsplit.features.main.group.widgets.ParticipantView
import com.mikkelthygesen.billsplit.features.main.add_expense.views.SharedExpensesView
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import kotlin.math.roundToInt


internal val SCROLL_OFFSET = 200.dp.value

@Composable
fun ExpenseView() {
    val addExpenseViewModel: AddExpenseViewModel = viewModel()
    val groupExpense = addExpenseViewModel.groupExpense
    val sharedExpense = groupExpense.sharedExpenseState
    val expenseHolders = groupExpense.individualExpenses

    val scrollState = rememberScrollState()
    var sharedExpensePositionY by remember {
        mutableStateOf(0F)
    }

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        BigTopBar(
            leadingContent = {
                BackButton {
                    addExpenseViewModel.onBackButtonPressed()
                }
            },
            trailingContent = {
                SimpleIconButton(iconResId = com.mikkelthygesen.billsplit.R.drawable.ic_check) {
                    addExpenseViewModel.saveExpense()
                }
            }
        )
        SharedExpensesView(
            modifier = Modifier.onGloballyPositioned {
                sharedExpensePositionY = it.positionInParent().y - SCROLL_OFFSET
            },
            onScrollToPosition = {
                scrollState.animateScrollTo(sharedExpensePositionY.roundToInt())
            },
            groupExpense = groupExpense,
            onChangeListener = { groupExpense.sharedExpenseState = it }
        )
        expenseHolders.map { individualExpenseHolder ->
            val numOfParticipants = expenseHolders.count { it.isParticipantState }
            var positionY = 0f
            ParticipantView(
                modifier = Modifier.onGloballyPositioned {
                    positionY = it.positionInParent().y - SCROLL_OFFSET
                },
                expenseHolder = individualExpenseHolder,
                groupExpense = groupExpense,
                sharedExpense = sharedExpense / numOfParticipants,
                onChangeListener = { individualExpenseHolder.expenseState = it },
                onRemoveClicked = {},
                onScrollToPosition = {
                    scrollState.animateScrollTo(positionY.roundToInt())
                }
            )
        }
        ExpenseViewHeader(groupExpense)
        Box(modifier = Modifier.height(100.dp))
    }
}