package com.mikkelthygesen.billsplit.features.main.add_expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffoldWithAuth
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.widgets.ConfirmChangesDialog
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.fmt2dec
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseFragment : Fragment() {

    private val addExpenseViewModel: AddExpenseViewModel by viewModels()
    private val groupId: String by lazy { arguments?.getString(ARG_GROUP_ID) ?: "" }
    private val expenseId: String by lazy { arguments?.getString(ARG_EXPENSE_ID) ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                addExpenseViewModel.loadExpense(groupId, expenseId)
                val uiStateFlow = addExpenseViewModel.uiStateFlow.collectAsState()

                when (addExpenseViewModel.dialogState) {
                    is AddExpenseViewModel.ConfirmDialog ->
                        ConfirmChangesDialog()
                }

                BaseScaffoldWithAuth(baseViewModel = addExpenseViewModel,
                    topBar = {
                        BigTopBar(
                            leadingContent = {
                                BackButton {
                                    addExpenseViewModel.onBackButtonPressed()
                                }
                            },
                            title = if (uiStateFlow.value is AddExpenseViewModel.ExpenseLoaded)
                                "$${addExpenseViewModel.groupExpense.total.fmt2dec()}" else "",
                            trailingContent = {
                                SimpleIconButton(iconResId = R.drawable.ic_check) {
                                    addExpenseViewModel.saveExpense()
                                }
                            }
                        )
                    }) {
                    Crossfade(targetState = uiStateFlow.value) { uiState ->
                        when (uiState) {
                            is BaseViewModel.UiState.Loading -> LoadingView()
                            is AddExpenseViewModel.ExpenseLoaded -> ExpenseView()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(addExpenseViewModel.uiEventsState) { event ->
            when (event) {
                is BaseViewModel.UiEvent.OnBackPressed -> {
                    if (!addExpenseViewModel.handleBack()) {
                        popBackStack()
                    }
                }
                is AddExpenseViewModel.ExpenseSaved -> {
                    popBackStack()
                }
            }
        }
    }

    companion object {
        const val ARG_GROUP_ID = "arg_group_id"
        const val ARG_EXPENSE_ID = "arg_expense_id"
    }
}