package com.mikkelthygesen.billsplit.features.main.group.services.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.domain.models.SubscriptionService
import com.mikkelthygesen.billsplit.ui.widgets.CircularUrlImageView

@Composable
fun ServiceListItem(service: SubscriptionService, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularUrlImageView(
            modifier = Modifier.size(64.dp),
            imageUrl = service.imageUrl
        )
        Column {
            Text(text = service.nameState)
            Text(text = "${service.monthlyExpenseState} is paid by ${service.payerState.nameState}")
        }
    }
}