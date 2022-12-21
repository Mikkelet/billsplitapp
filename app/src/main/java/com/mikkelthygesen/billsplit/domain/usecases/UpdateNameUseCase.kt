package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import javax.inject.Inject

class UpdateNameUseCase @Inject constructor(
    private val authProvider: AuthProvider
) {

    suspend fun execute() {
        authProvider.updateUserName()
    }
}