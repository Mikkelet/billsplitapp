        package com.mikkelthygesen.billsplit.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.ui.features.group.GroupViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FabView(
    viewModel: GroupViewModel = viewModel(),
    @DrawableRes iconResId: Int = R.drawable.ic_add_plus
) {
    FloatingActionButton(
        onClick = viewModel::addExpense,
        modifier = Modifier.padding(16.dp, 32.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId), contentDescription = "Add"
        )
    }
}

@Preview
@Composable
fun PreviewFabView(){
    FabView()
}