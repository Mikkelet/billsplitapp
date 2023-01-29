package com.mikkelthygesen.billsplit.features.main.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.R
import com.mikkelthygesen.billsplit.features.base.BaseScaffold
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.group.add_expense.ExpenseView
import com.mikkelthygesen.billsplit.features.main.group.group_view.GroupEventsView
import com.mikkelthygesen.billsplit.features.main.group.view_expenses.ViewDebt
import com.mikkelthygesen.billsplit.features.main.group.widgets.ConfirmChangesDialog
import com.mikkelthygesen.billsplit.features.main.group.widgets.GroupBottomBar
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.ErrorDialog
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import com.mikkelthygesen.billsplit.ui.widgets.SimpleIconButton
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
                    topBar = {
                        TopAppBar(
                            title = {
                                if (uiState is BaseViewModel.UiState.Loading)
                                    Text(text = "")
                                else Text(text = viewModel.group.nameState)
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "")
                                }
                            },
                            actions = {
                                if (uiState is GroupViewModel.EditExpense)
                                    SimpleIconButton(
                                        iconResId = R.drawable.ic_check,
                                        tint = MaterialTheme.colors.onBackground
                                    ) {
                                        viewModel.saveGroupExpense(uiState.groupExpense)
                                    }
                                if (uiState is GroupViewModel.Chat)
                                    SimpleIconButton(
                                        iconResId = R.drawable.ic_baseline_settings_24,
                                        tint = MaterialTheme.colors.onBackground
                                    ) {
                                        // show settings
                                    }
                                if (uiState is GroupViewModel.Chat)
                                    SimpleIconButton(
                                        iconResId = R.drawable.ic_money,
                                        tint = MaterialTheme.colors.onBackground,
                                        onClick = viewModel::showDebt
                                    )
                            }
                        )
                    }
                ) {
                    Box {
                        Crossfade(
                            targetState = uiState
                        ) { state ->
                            when (state) {
                                is BaseViewModel.UiState.Loading -> LoadingView()
                                is GroupViewModel.Chat -> GroupEventsView()
                                is GroupViewModel.ShowDebt -> ViewDebt()
                                is GroupViewModel.EditExpense -> ExpenseView(groupExpense = state.groupExpense)
                            }
                        }
                        if (viewModel.showChatLoader)
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(top = 24.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.background)
                                    .padding(4.dp)
                                    .align(Alignment.TopCenter)
                            )
                    }
                }
            }
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