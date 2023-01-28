package com.mikkelthygesen.billsplit.features.main.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikkelthygesen.billsplit.features.main.popBackStack
import com.mikkelthygesen.billsplit.ui.theme.BillSplitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingFragment : Fragment() {

    private val viewModel: LandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                BillSplitTheme {

                    val uiStateFlow = viewModel.uiStateFlow.collectAsState()
                    val uiState = uiStateFlow.value
                    Crossfade(targetState = uiState) { state ->
                        when (state) {
                            is LandingViewModel.SignIn -> SignInView(
                                onSignInWithCredentials = viewModel::signInEmail,
                                onSignInWithFacebookClicked = {},
                                onSignInWithGoogleClicked = {},
                                onAlreadySignedUpClicked = viewModel::showSignUp
                            )
                            is LandingViewModel.SignUp -> SignUpView(
                                onSignUpWithCredentials = viewModel::signUpEmail,
                                onSignUpWithGoogleClicked = { },
                                onSignUpWithFacebookClicked = { },
                                onAlreadyHaveAccount = viewModel::showSignIn
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authProvider.userLiveData.observe(requireActivity()) {
            if(it != null)
                popBackStack()
        }
    }
}