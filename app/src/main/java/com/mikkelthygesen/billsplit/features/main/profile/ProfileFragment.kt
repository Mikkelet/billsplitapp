package com.mikkelthygesen.billsplit.features.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.collectEvents
import com.mikkelthygesen.billsplit.features.base.BaseScaffold
import com.mikkelthygesen.billsplit.features.main.navigateToFriends
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.features.main.widgets.BigTopBar
import com.mikkelthygesen.billsplit.ui.widgets.BackButton
import com.mikkelthygesen.billsplit.ui.widgets.RequireUserView
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
                BaseScaffold(
                    topBar = {
                        BigTopBar(
                            leadingContent = {
                                BackButton {
                                    popBackStack()
                                }
                            }
                        )
                    },
                ) {
                    RequireUserView(baseViewModel = profileViewModel) { user ->
                        ProfileView(
                            user = user,
                            onUpdateUser = profileViewModel::updateName,
                            onError = profileViewModel::handleError
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents(profileViewModel.uiEventsState) { event ->
            when (event) {
                is ProfileViewModel.FriendsPressed -> {
                    navigateToFriends()
                }
            }
        }
    }
}