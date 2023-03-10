package com.mikkelthygesen.billsplit.features.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffoldWithAuth
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.features.main.navigateToFriends
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BaseScaffoldWithAuth(
                    baseViewModel = profileViewModel,
                    topBar = {
                        BigTopBar(
                            leadingContent = {
                                BackButton(onClick = ::popBackStack)
                            }
                        )
                    },
                ) {
                    ProfileView(
                        onUpdateUser = profileViewModel::updateName,
                        onError = profileViewModel::handleError
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(profileViewModel.uiEventsState) { event ->
            when (event) {
                is BaseViewModel.UiEvent.OnBackPressed -> {
                    popBackStack()
                }
                is ProfileViewModel.FriendsPressed -> {
                    navigateToFriends()
                }
            }
        }
    }
}