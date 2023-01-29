package com.mikkelthygesen.billsplit.features.main.add_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffold
import com.mikkelthygesen.billsplit.features.main.navigateToFriends
import com.mikkelthygesen.billsplit.features.main.navigateToGroup
import com.mikkelthygesen.billsplit.features.main.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupFragment : Fragment() {

    private val addGroupViewModel: AddGroupViewModel by viewModels()
    private val group by lazy { addGroupViewModel.group }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val uiStateFlow = addGroupViewModel.uiStateFlow.collectAsState()
                val uiState = uiStateFlow.value
                BaseScaffold {
                    AddGroupView(
                        uiState = uiState,
                        group = group,
                        onNext = addGroupViewModel::next,
                        onBack = {
                            if (uiState is AddGroupViewModel.AddName)
                                popBackStack()
                            else addGroupViewModel.back()
                        },
                        onClose = ::popBackStack,
                        onSubmitGroup = addGroupViewModel::saveGroup,
                        showSubmitLoader = addGroupViewModel.submittingGroup
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(addGroupViewModel.uiEventsState) {
            when (it) {
                is AddGroupViewModel.ShowFriendsPressed -> {
                    navigateToFriends()
                }
                is AddGroupViewModel.GroupAdded -> {
                    popBackStack()
                    navigateToGroup(it.group.id)
                }
            }
        }
    }
}