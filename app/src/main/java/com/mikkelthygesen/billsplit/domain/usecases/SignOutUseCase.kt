package com.mikkelthygesen.billsplit.domain.usecases

import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.domain.repositories.DatabaseRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SignOutUseCase @Inject constructor(
    private val authProvider: AuthProvider,
    private val databaseRepository: DatabaseRepository
) {

    suspend fun execute() {
        databaseRepository.clearDatabase()
        authProvider.signOut()
    }
}