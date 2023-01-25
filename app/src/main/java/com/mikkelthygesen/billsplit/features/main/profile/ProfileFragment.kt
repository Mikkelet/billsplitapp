package com.mikkelthygesen.billsplit.features.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
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
                BillSplitTheme {
                    Scaffold {
                        val padding = it
                        RequireUserView(baseViewModel = profileViewModel) { user ->
                            ProfileView(
                                user = user,
                                onUpdateUser = {},
                                onError = {}
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()

    }
}