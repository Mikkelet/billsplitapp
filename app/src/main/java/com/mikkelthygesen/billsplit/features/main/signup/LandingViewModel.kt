package com.mikkelthygesen.billsplit.features.main.signup

import androidx.lifecycle.viewModelScope
import com.mikkelthygesen.billsplit.domain.usecases.SignInWithEmailUseCase
import com.mikkelthygesen.billsplit.domain.usecases.SignUpWithEmailUseCase
import com.mikkelthygesen.billsplit.features.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
) : BaseViewModel() {
    object SignIn : UiState
    object SignUp : UiState

    override val _mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(SignIn)

    fun signUpEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = runCatching { signUpWithEmailUseCase.execute(email, password) }
            result.foldSuccess {  }
        }
    }

    fun signInEmail(email: String, password: String) {
        updateUiState(UiState.Loading)
        viewModelScope.launch {
            val result = runCatching { signInWithEmailUseCase.execute(email, password) }
            result.foldSuccess {  }
        }
    }

    fun showSignIn() {
        updateUiState(SignIn)
    }

    fun showSignUp() {
        updateUiState(SignUp)
    }
}