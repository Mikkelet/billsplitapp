package com.mikkelthygesen.billsplit.ui.features.group.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.models.GroupExpense
import com.mikkelthygesen.billsplit.sampleSharedExpenses
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel


@Composable
fun ExpenseViewHeader(
    groupExpense: GroupExpense,
    groupViewModel: GroupViewModel = viewModel()
) {
    _ExpenseViewHeader(groupExpense = groupExpense) {
        if (groupExpense.isChanged())
            groupViewModel.showConfirmChangesDialog(groupExpense)
        else
            groupViewModel.showChat()
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun _ExpenseViewHeader(
    groupExpense: GroupExpense,
    onBackPressed: () -> Unit
) {
    Box(Modifier.padding(bottom = 32.dp)) {
        Image(
            painter = painterResource(id = R.drawable.best_restraunts),
            contentDescription = "",
        )
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