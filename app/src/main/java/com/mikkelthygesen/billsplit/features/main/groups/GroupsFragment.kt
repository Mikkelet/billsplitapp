package com.mikkelthygesen.billsplit.features.main.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffoldWithAuth
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.features.main.navigateToAddGroup
import com.mikkelthygesen.billsplit.features.main.navigateToGroup
import com.mikkelthygesen.billsplit.features.main.navigateToProfile
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
                val groupsUiStateFlow = groupsViewModel.uiStateFlow.collectAsState()
                val mainUiStateFlow = mainViewModel.uiStateFlow.collectAsState()
                val mainUiState = mainUiStateFlow.value

                // System ui color changes
                /* Dark mode doesn't work yet
                val systemUiController = rememberSystemUiController()
                systemUiController.systemBarsDarkContentEnabled = isSystemInDarkTheme()
                systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
                systemUiController.setNavigationBarColor(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                        3.dp
                    )
                )
                 */

                val lazyListState = rememberLazyListState()
                val firstVisibleIndexFlow = snapshotFlow { lazyListState.firstVisibleItemIndex }
                    .collectAsState(initial = 0)

                BaseScaffoldWithAuth(
                    baseViewModel = groupsViewModel,
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            modifier = Modifier.padding(32.dp),
                            onClick = {
                                navigateToAddGroup()
                            },
                            expanded = firstVisibleIndexFlow.value == 0,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            text = { Text(text = "Group") },
                            icon = { Icon(Icons.Filled.Add, contentDescription = "Add Group") }
                        )
                    },
                ) {
                    GroupsList(
                        uiState = groupsUiStateFlow.value,
                        lazyListState = lazyListState,
                        isUserSynchronizing = mainUiState is BaseViewModel.UiState.Loading
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