package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authProvider: AuthProvider
) {
    suspend fun execute(email: String, password: String) {
        authProvider.signInWithEmail(email, password)
    }
}