package com.mikkelthygesen.billsplit.features.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.group.add_expense.ExpenseView
import com.mikkelthygesen.billsplit.features.group.group_view.GroupEventsView
import com.mikkelthygesen.billsplit.features.group.view_expenses.ViewDebt
import com.mikkelthygesen.billsplit.features.group.widgets.ConfirmChangesDialog
import com.mikkelthygesen.billsplit.features.group.widgets.GroupBottomBar
import com.mikkelthygesen.billsplit.features.group.widgets.GroupTopBar
import com.mikkelthygesen.billsplit.features.main.Screen
import com.mikkelthygesen.billsplit.features.main.navigate
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.features.main.widgets.dialogs.ErrorDialog
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : Fragment() {

    private val viewModel: GroupViewModel by viewModels()
    private val groupId by lazy { arguments?.getString("group_id") ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getGroup(groupId)
        return ComposeView(requireContext()).apply {
            setContent {
                BillSplitTheme {
                    val groupUiState = viewModel.uiStateFlow.collectAsState()
                    val uiState = groupUiState.value

                    BackHandler {
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

                    Scaffold(
                        bottomBar = { GroupBottomBar(uiState) }
                    ) {
                        Box {
                            Crossfade(
                                targetState = uiState,
                                modifier = Modifier.padding(it)
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
                            GroupTopBar(
                                modifier = Modifier.align(Alignment.TopCenter)
                            ) {
                                requireActivity().onBackPressedDispatcher.onBackPressed()
                            }
                        }
                    }
                }
            }

        }
    }

    companion object {
        fun newInstance(groupId: String): GroupFragment {
            val args = Bundle()
            args.putString("group_id", groupId)
            val fragment = GroupFragment()
            fragment.arguments = args
            return fragment
        }
    }
}