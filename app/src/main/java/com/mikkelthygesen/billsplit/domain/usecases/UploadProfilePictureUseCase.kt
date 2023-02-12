package com.mikkelthygesen.billsplit.domain.usecases

import android.net.Uri
import com.mikkelthygesen.billsplit.data.remote.auth.AuthProvider
import com.mikkelthygesen.billsplit.data.remote.storage.StorageProvider
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class UploadProfilePictureUseCase @Inject constructor(
    private val authProvider: AuthProvider,
    private val storageProvider: StorageProvider
) {

    suspend fun execute(uri: Uri) {
        val loggedInUser = authProvider.requireLoggedInUser
        val downloadUrl = storageProvider.uploadPhoto(loggedInUser.uid, uri)
        authProvider.updateProfilePicture(downloadUrl)
        loggedInUser.pfpUrlState = downloadUrl.toString()
    }
}