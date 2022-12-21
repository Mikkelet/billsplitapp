package com.mikkelthygesen.billsplit.data.remote.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.mikkelthygesen.billsplit.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageProvider @Inject constructor() {
    private val firebaseStorage by lazy {
        FirebaseStorage.getInstance().apply {
            if (BuildConfig.FLAVOR == "emulator")
                useEmulator("10.0.2.2", 9099)
        }
    }

    suspend fun uploadPhoto(userId: String, uri: Uri): Uri {
        val snapshot = firebaseStorage
            .getReference("${userId}/${uri.lastPathSegment}")
            .putFile(uri).await()
        return snapshot.storage.downloadUrl.await()
    }
}