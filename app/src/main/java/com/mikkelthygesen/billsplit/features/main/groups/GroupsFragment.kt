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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.features.base.BaseScaffold
import com.mikkelthygesen.billsplit.features.main.MainViewModel
import com.mikkelthygesen.billsplit.features.main.Screen
import com.mikkelthygesen.billsplit.features.main.navigate
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
        groupsViewModel.getGroups(false)

        return ComposeView(requireContext()).apply {
            setContent {

                LaunchedEffect(key1 = Unit, block = {
                    groupsViewModel.uiEventsState.collect { uiEvents ->
                        when (uiEvents) {
                            is GroupsViewModel.ShowProfile -> {
                                navigate(Screen.Profile, Screen.Groups)
                            }
                            is GroupsViewModel.ShowGroup -> {
                                val args = Bundle()
                                args.putString("group_id", uiEvents.group.id)
                                navigate(Screen.Group, Screen.Groups, args)
                            }
                            is GroupsViewModel.AddGroup -> {
                                navigate(Screen.AddGroup, Screen.Groups)
                            }
                        }
                    }
                })

                BaseScaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier.padding(32.dp), onClick = {
                                navigate(Screen.AddGroup, Screen.Groups)
                            }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add Group")
                        }
                    },
                ) {
                    val uiStateFlow = groupsViewModel.uiStateFlow.collectAsState()
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

    companion object {
        fun newInstance(): GroupsFragment = GroupsFragment()
    }
}