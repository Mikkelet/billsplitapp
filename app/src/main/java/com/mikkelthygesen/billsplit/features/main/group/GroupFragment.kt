package com.mikkelthygesen.billsplit.features.main.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffold
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.add_expense.ExpenseView
import com.mikkelthygesen.billsplit.features.main.group.group_view.GroupEventsView
import com.mikkelthygesen.billsplit.features.main.group.services.ServicesView
import com.mikkelthygesen.billsplit.features.main.group.view_expenses.ViewDebt
import com.mikkelthygesen.billsplit.features.main.group.widgets.ConfirmChangesDialog
import com.mikkelthygesen.billsplit.features.main.group.widgets.GroupBottomBar
import com.mikkelthygesen.billsplit.features.main.group.widgets.GroupTopBar2
import com.mikkelthygesen.billsplit.features.main.group.widgets.TopLoader
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.ErrorDialog
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

                BackHandler {
                    handleBack(uiState)
                }

                when (val state = viewModel.dialogState) {
                    is GroupViewModel.ConfirmChangesDialog -> ConfirmChangesDialog(
                        groupExpense = state.groupExpense
                    )
                    is BaseViewModel.DialogState.Error -> ErrorDialog(
                        exception = state.exception,
                        onDismiss = viewModel::dismissDialog
                    )
                    is BaseViewModel.DialogState.DismissDialogs -> Unit
                }

                BaseScaffold(
                    bottomBar = { GroupBottomBar(uiState) },
                    topBar = { GroupTopBar2() }
                ) {
                    Box {
                        Crossfade(
                            targetState = uiState
                        ) { state ->
                            when (state) {
                                is BaseViewModel.UiState.Loading -> LoadingView()
                                is GroupViewModel.Chat -> GroupEventsView()
                                is GroupViewModel.ShowDebt -> ViewDebt()
                                is GroupViewModel.Services -> ServicesView()
                                is GroupViewModel.EditExpense -> ExpenseView(
                                    groupExpense = state.groupExpense,
                                )
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
            if (uiEvent is BaseViewModel.UiEvent.OnBackPressed)
                requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun handleBack(uiState: BaseViewModel.UiState) {
        when (uiState) {
            is GroupViewModel.EditExpense -> {
                if (uiState.groupExpense.isChanged())
                    viewModel.showConfirmChangesDialog(uiState.groupExpense)
                else viewModel.showChat()
            }
            is GroupViewModel.ShowDebt -> viewModel.showChat()
            else -> popBackStack()
        }
    }

    companion object {
        const val ARG_GROUP_ID = "group_id"
    }
}