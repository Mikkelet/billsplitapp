package com.mikkelthygesen.billsplit.features.group.add_expense

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.features.group.widgets.ExpenseViewHeader
import com.mikkelthygesen.billsplit.features.group.widgets.ParticipantView
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.features.group.widgets.SharedExpensesView
import kotlin.math.roundToInt


private val SCROLL_OFFSET = 200.dp.value

@Composable
fun ExpenseView(
    groupExpense: GroupExpense,
) {
    val sharedExpense = groupExpense.sharedExpenseState
    val expenseHolders = groupExpense.individualExpenses

    val scrollState = rememberScrollState()
    var sharedExpensePositionY by remember {
        mutableStateOf(0F)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            ExpenseViewHeader(groupExpense)
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
            Box(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview
@Composable
fun PreviewExpenseView() {
    ExpenseView(groupExpense = sampleSharedExpenses.first())
}