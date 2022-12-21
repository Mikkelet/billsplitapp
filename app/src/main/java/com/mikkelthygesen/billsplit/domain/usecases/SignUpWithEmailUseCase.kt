package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import javax.inject.Inject

class SignUpWithEmailUseCase @Inject constructor(
    private val authProvider: AuthProvider
) {
    suspend fun execute(email: String, password: String) {
        authProvider.signUpWithEmail(email, password)
    }
}