package com.mikkelthygesen.billsplit.features.main.group.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.group.services.views.CenteredMessage
import com.mikkelthygesen.billsplit.features.main.group.services.views.ServiceListItem

@Composable
fun ServicesView() {
    val groupViewModel: GroupViewModel = viewModel()
    val servicesFlow = groupViewModel.servicesFlow().collectAsState(initial = emptyList())
    val services = servicesFlow.value
    if (services.isEmpty())
        CenteredMessage(text = "You have not added any subscription services")
    else
        LazyColumn(Modifier.fillMaxSize()) {
            items(services.size) { index ->
                val service = services[index]
                ServiceListItem(service = service) {
                    groupViewModel.onServiceClicked(service)
                }
            }
            item {
                Box(Modifier.height(80.dp))
            }
        }
}

@Composable
private fun Title() {
    Text(
        modifier = Modifier
            .padding(32.dp),
        text = "Services",
        style = MaterialTheme.typography.h5
    )
}