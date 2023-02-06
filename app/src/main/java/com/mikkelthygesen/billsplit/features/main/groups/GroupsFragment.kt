package com.mikkelthygesen.billsplit.features.main.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffold
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.features.main.navigateToAddGroup
import com.mikkelthygesen.billsplit.features.main.navigateToGroup
import com.mikkelthygesen.billsplit.features.main.navigateToProfile
import com.mikkelthygesen.billsplit.ui.widgets.RequireUserView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : Fragment() {

    private val groupsViewModel: GroupsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                groupsViewModel.getGroups(false)
                val uiStateFlow = groupsViewModel.uiStateFlow.collectAsState()
                BaseScaffold(
                    baseViewModel = groupsViewModel,
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier.padding(32.dp), onClick = {
                                navigateToAddGroup()
                            }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Group")
                        }
                    },
                ) {
                    RequireUserView(baseViewModel = mainViewModel) { user ->
                        GroupsList(
                            uiState = uiStateFlow.value,
                            user = user
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(groupsViewModel.uiEventsState) { event ->
            when (event) {
                is GroupsViewModel.ShowProfile -> {
                    navigateToProfile()
                }
                is GroupsViewModel.ShowGroup -> {
                    navigateToGroup(event.group.id)
                }
                is GroupsViewModel.AddGroup -> {
                    navigateToAddGroup()
                }
            }
        }
    }

    companion object {
        fun newInstance(): GroupsFragment = GroupsFragment()
    }
}