package com.mikkelthygesen.billsplit.features.main.add_expense.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.GroupExpense
import com.mikkelthygesen.billsplit.features.main.add_expense.AddExpenseViewModel
import com.mikkelthygesen.billsplit.features.main.group.widgets.DescriptionTextField
import com.mikkelthygesen.billsplit.sampleSharedExpenses


@Composable
fun ExpenseViewHeader(
    groupExpense: GroupExpense,
    addExpenseViewModel: AddExpenseViewModel = viewModel()
) {
    _ExpenseViewHeader(groupExpense = groupExpense) {
        addExpenseViewModel.onBackButtonPressed()
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun _ExpenseViewHeader(
    groupExpense: GroupExpense,
    onBackPressed: () -> Unit
) {
    Box(Modifier.padding(bottom = 32.dp)) {
        DescriptionTextField(
            Modifier.align(Alignment.BottomStart),
            initialValue = groupExpense.descriptionState
        ) {
            groupExpense.descriptionState = it
        }
    }
}

@Preview
@Composable
private fun Preview() {
    _ExpenseViewHeader(groupExpense = sampleSharedExpenses.first()) {}
}