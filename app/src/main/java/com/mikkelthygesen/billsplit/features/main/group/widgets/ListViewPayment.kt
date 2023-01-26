package com.mikkelthygesen.billsplit.features.main.group.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.domain.models.Payment
import com.mikkelthygesen.billsplit.domain.models.Person


@Composable
fun ListViewPayment(payment: Payment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${payment.createdBy.nameState} paid $${payment.amount.fmt2dec()} to ${payment.paidTo.nameState}",
            style = TextStyle(color = Color.Gray)
        )
    }
}


@Preview
@Composable
private fun Preview(){
    val person = Person("321","Mikkel")
    val payment = Payment("1234", person, person, 1000F)
    ListViewPayment(payment = payment)
}

