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
import com.mikkelthygesen.billsplit.features.base.BaseScaffold
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import com.mikkelthygesen.billsplit.ui.widgets.LoadingView
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
                BaseScaffold(baseViewModel = viewModel) {
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
                            is BaseViewModel.UiState.Loading -> LoadingView()
                        }
                    }
                }
            }
        }
    }
}