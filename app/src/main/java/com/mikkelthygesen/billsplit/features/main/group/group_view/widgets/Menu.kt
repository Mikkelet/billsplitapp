package com.mikkelthygesen.billsplit.features.main.group.group_view.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel


@Composable
fun Menu(
    viewModel: GroupViewModel = viewModel(),
) {
    _Menu {
        viewModel.addExpense()
    }
}

@Composable
@SuppressLint("ComposableNaming")
private fun _Menu(
    onAddExpense: () -> Unit
) {
    Row(
        Modifier
            .padding(bottom = 24.dp, top = 8.dp)
            .wrapContentWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onAddExpense() }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_plus),
            contentDescription = "Add expense",
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Box(Modifier.width(8.dp))
        Text(text = "New Expense", color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(){
    _Menu {}
}