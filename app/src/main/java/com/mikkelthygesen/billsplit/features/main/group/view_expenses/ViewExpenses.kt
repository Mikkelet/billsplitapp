package com.mikkelthygesen.billsplit.features.main.group.view_expenses

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.domain.models.Person
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.group.services.views.CenteredMessage
import com.mikkelthygesen.billsplit.features.main.group.view_expenses.widgets.DebtView
import com.mikkelthygesen.billsplit.ui.widgets.*

@Composable
fun ViewDebt() {
    val groupViewModel: GroupViewModel = viewModel()
    val debts = groupViewModel.debtFlow().collectAsState(initial = emptyList())
    _ViewDebt(
        debt = debts.value
    )
}

@Preview(showSystemUi = true)
@Composable
fun TestPreview() {
    Test(user = Person())
}

@Composable
fun Test(user: Person, viewModel: GroupViewModel = hiltViewModel()) {
    Text(text = "hello world")
}


@Composable
@SuppressLint("ComposableNaming")
private fun _ViewDebt(
    debt: List<Pair<Person, Float>>
) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        if (debt.none { it.second != 0F })
            CenteredMessage("All debts are settled")
        else
            (debt)
                .sortedBy { it.second }
                .reversed().map {
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (it.second != 0F)
                            DebtView(debt = it)
                    }
                }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewViewExpense() {
    _ViewDebt(
        emptyList()
    )
}

