package com.mikkelthygesen.billsplit.features.main.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffoldWithAuth
import com.mikkelthygesen.billsplit.features.main.navigateToAddGroup
import com.mikkelthygesen.billsplit.features.main.navigateToGroup
import com.mikkelthygesen.billsplit.features.main.navigateToProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : Fragment() {

    private val groupsViewModel: GroupsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val uiStateFlow = groupsViewModel.uiStateFlow.collectAsState()
                BaseScaffoldWithAuth(
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
                    GroupsList(
                        uiState = uiStateFlow.value,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(groupsViewModel.uiEventsState) { event ->
            when (event) {
                is GroupsViewModel.OnProfileClicked -> {
                    navigateToProfile()
                }
                is GroupsViewModel.OnGroupClicked -> {
                    navigateToGroup(event.group.id)
                }
                is GroupsViewModel.AddGroupClicked -> {
                    navigateToAddGroup()
                }
            }
        }
    }

    companion object {
        fun newInstance(): GroupsFragment = GroupsFragment()
    }
}