package com.mikkelthygesen.billsplit.features.main.group.services

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.group.services.views.ServiceListItem

@Composable
fun ServicesView() {
    val groupViewModel: GroupViewModel = viewModel()
    val servicesStateFlow = groupViewModel.servicesStateFlow.collectAsState()
    val services = servicesStateFlow.value
    LazyColumn(Modifier.fillMaxSize()) {
        items(services.size) { index ->
            val service = services[index]
            ServiceListItem(service = service) {
                groupViewModel.onServiceClicked(service)
            }
        }
    }
}