package com.mikkelthygesen.billsplit.data.remote.auth

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.UserProfileChangeRequest
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.data.remote.exceptions.NetworkExceptions
import com.mikkelthygesen.billsplit.models.Person
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProvider @Inject constructor() {

    private val firebase by lazy {
        FirebaseAuth.getInstance().apply {
            if (BuildConfig.FLAVOR == "emulator")
                useEmulator("10.0.2.2", 9099)
        }
    }

    var loggedInUser: Person? = null
        private set(value) {
            println("qqq value=$value")
            field = value
        }

    private val authListener = AuthStateListener {
        val user = it.currentUser
        loggedInUser = user?.let {
            Person(
                it.uid,
                name = it.displayName ?: "Splittsby User",
                pfpUrl = it.photoUrl?.toString() ?: "",
                email = it.email ?: ""
            )
        }
    }

    fun onCreate() {
        firebase.addAuthStateListener(authListener)
    }

    fun onDestroy() {
        firebase.removeAuthStateListener(authListener)
    }

    suspend fun updateUserName() {
        val currentUser = firebase.currentUser ?: throw NetworkExceptions.UserLoggedOutException
        val loggedInUser = loggedInUser ?: throw NetworkExceptions.UserLoggedOutException
        val request = UserProfileChangeRequest.Builder()
        request.displayName = loggedInUser.nameState
        currentUser.updateProfile(request.build()).await()
        loggedInUser.saveChanges()
    }

    @Suppress("KotlinConstantConditions")
    suspend fun updateProfilePicture(downloadUrl: Uri) {
        val currentUser = firebase.currentUser ?: throw NetworkExceptions.UserLoggedOutException
        val updateRequest = UserProfileChangeRequest.Builder()
        updateRequest.photoUri = downloadUrl
        currentUser.updateProfile(updateRequest.build()).await()
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
        val currentUser = firebase.currentUser ?: throw NetworkExceptions.UserLoggedOutException
        val result = currentUser.getIdToken(force).await()
        return result.token ?: throw NetworkExceptions.UserLoggedOutException
    }
}