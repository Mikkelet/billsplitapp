package com.mikkelthygesen.billsplit.features.main.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffoldWithAuth
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.*
import com.mikkelthygesen.billsplit.features.main.group.group_view.GroupEventsView
import com.mikkelthygesen.billsplit.features.main.group.services.ServicesView
import com.mikkelthygesen.billsplit.features.main.group.view_expenses.ViewDebt
import com.mikkelthygesen.billsplit.features.main.group.widgets.GroupBottomBar
import com.mikkelthygesen.billsplit.features.main.group.widgets.GroupTopBar2
import com.mikkelthygesen.billsplit.features.main.group.widgets.TopLoader
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : Fragment() {

    private val viewModel: GroupViewModel by viewModels()
    private val groupId by lazy { arguments?.getString(ARG_GROUP_ID) ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getGroup(groupId)

        return ComposeView(requireContext()).apply {
            setContent {
                val groupUiState = viewModel.uiStateFlow.collectAsState()
                val uiState = groupUiState.value
                val lazyListState = rememberLazyListState()
                val firstVisibleIndexFlow = snapshotFlow { lazyListState.firstVisibleItemIndex }.collectAsState(0)

                BaseScaffoldWithAuth(
                    baseViewModel = viewModel,
                    bottomBar = { GroupBottomBar(uiState) },
                    topBar = { GroupTopBar2() },
                    floatingActionButton = {
                        if (uiState is GroupViewModel.Services || uiState is GroupViewModel.Chat)
                            ExtendedFloatingActionButton(
                                modifier = Modifier.padding(16.dp),
                                onClick = {
                                    if (uiState is GroupViewModel.Services)
                                        navigateToAddService(groupId = groupId)
                                    else navigateToAddExpense(groupId)
                                },
                                expanded = firstVisibleIndexFlow.value == 0,
                                icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                                text = {
                                    val text = when (uiState) {
                                        is GroupViewModel.Services -> "Service"
                                        else -> "Expense"
                                    }
                                    Text(text = text)
                                },
                            )
                    }
                ) {
                    Box {
                        Crossfade(
                            targetState = uiState
                        ) { state ->
                            when (state) {
                                is BaseViewModel.UiState.Loading -> LoadingView()
                                is GroupViewModel.Chat -> GroupEventsView(
                                    groupId = groupId,
                                    lazyListState = lazyListState
                                )
                                is GroupViewModel.ShowDebt -> ViewDebt()
                                is GroupViewModel.Services -> ServicesView()
                            }
                        }
                        TopLoader(
                            modifier = Modifier.align(Alignment.TopCenter),
                            show = viewModel.showChatLoader
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(viewModel.uiEventsState) { uiEvent ->
            when (uiEvent) {
                is BaseViewModel.UiEvent.OnBackPressed -> if (!viewModel.handleBack()) popBackStack()
                is GroupViewModel.OnServiceClicked -> navigateToEditService(
                    groupId,
                    uiEvent.service.id
                )
                is GroupViewModel.OnAddExpenseClicked -> navigateToAddExpense(viewModel.group.id)
                is GroupViewModel.OnEditExpenseClicked -> navigateToEditExpense(
                    viewModel.group.id,
                    uiEvent.expenseId
                )
            }
        }
    }

    companion object {
        const val ARG_GROUP_ID = "group_id"
    }
}