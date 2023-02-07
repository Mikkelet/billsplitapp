package com.mikkelthygesen.billsplit.features.main.group.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikkelthygesen.billsplit.features.main.group.GroupViewModel
import com.mikkelthygesen.billsplit.features.main.group.services.views.CenteredMessage
import com.mikkelthygesen.billsplit.features.main.group.services.views.ServiceListItem
import com.mikkelthygesen.billsplit.ui.widgets.ErrorView
import com.mikkelthygesen.billsplit.ui.widgets.FutureComposable
import com.mikkelthygesen.billsplit.ui.widgets.FutureState
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView

@Composable
fun ServicesView() {
    val groupViewModel: GroupViewModel = viewModel()

    Column {
        Text(
            modifier = Modifier
                .padding(32.dp),
            text = "Services",
            style = MaterialTheme.typography.h5
        )
        FutureComposable(asyncCallback = {
            groupViewModel.getLocalServices()
        }) { state, _ ->
            when (state) {
                is FutureState.Loading -> LoadingView()
                is FutureState.Failure -> ErrorView(error = state.error)
                is FutureState.Success -> {
                    val services = state.data
                    if (services.isEmpty())
                        CenteredMessage("You have not added any subscription services")
                    else
                        LazyColumn(Modifier.fillMaxSize()) {
                            items(services.size) { index ->
                                val service = services[index]
                                ServiceListItem(service = service) {
                                    groupViewModel.onServiceClicked(service)
                                }
                            }
                        }
                }
            }
        }
    }
}