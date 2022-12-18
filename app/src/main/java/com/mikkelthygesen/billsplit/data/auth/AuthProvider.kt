package com.mikkelthygesen.billsplit.data.auth

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.models.Person
import kotlinx.coroutines.tasks.await


val authProvider = AuthProvider()

class AuthProvider {

    private val firebase by lazy {
        FirebaseAuth.getInstance().apply {
            if (BuildConfig.FLAVOR == "emulator")
                useEmulator("10.0.2.2", 9099)
        }
    }

    private val firebaseStorage by lazy {
        FirebaseStorage.getInstance().apply {
            if (BuildConfig.FLAVOR == "emulator")
                useEmulator("10.0.2.2", 9099)
        }
    }

    var loggedInUser: Person? = null
        private set

    fun isUserLoggedIn(): Boolean {
        return loggedInUser != null
    }

    private val authListener = AuthStateListener {
        val user = it.currentUser
        if (user != null) {
            val person = Person(
                user.uid,
                name = user.displayName ?: "Splittsby User",
                pfpUrl = user.photoUrl?.toString() ?: "",
                email = user.email ?: ""
            )
            loggedInUser = person
        }
    }

    fun onCreate() {
        firebase.addAuthStateListener(authListener)
    }

    fun onDestroy() {
        firebase.removeAuthStateListener(authListener)
        loggedInUser = null
    }

    suspend fun updateUserName(name: String) {
        val currentUser = firebase.currentUser ?: throw NetworkExceptions.UserLoggedOut
        val request = UserProfileChangeRequest.Builder()
        request.displayName = name
        currentUser.updateProfile(request.build()).await()
    }

    @Suppress("KotlinConstantConditions")
    suspend fun updateProfilePicture(user: Person, uri: Uri) {
        val currentUser = firebase.currentUser ?: throw NetworkExceptions.UserLoggedOut
        val snapshot = firebaseStorage
            .getReference("${user.uid}/${uri.lastPathSegment}")
            .putFile(uri).await()
        val downloadUrl = snapshot.storage.downloadUrl.await()
        val updateRequest = UserProfileChangeRequest.Builder()
        updateRequest.photoUri = downloadUrl
        currentUser.updateProfile(updateRequest.build()).await()
        user.pfpUrlState = downloadUrl.toString()
    }

    suspend fun signUpWithEmail(email: String, password: String) {
        val result = firebase.createUserWithEmailAndPassword(email, password).await()
        onSignIn(result)
    }

    suspend fun signInWithEmail(email: String, password: String) {
        val result = firebase.signInWithEmailAndPassword(email, password).await()
        onSignIn(result)
    }

    private fun onSignIn(authResult: AuthResult) {
        val user = authResult.user
        if (user != null) {
            val person = Person(user.uid, name = user.displayName ?: "Unknown user")
            loggedInUser = person
        }
    }

    suspend fun getAuthToken(force: Boolean): String {
        val currentUser = firebase.currentUser ?: throw NetworkExceptions.UserLoggedOut
        val result = currentUser.getIdToken(force).await()
        return result.token ?: throw NetworkExceptions.UserLoggedOut
    }
}