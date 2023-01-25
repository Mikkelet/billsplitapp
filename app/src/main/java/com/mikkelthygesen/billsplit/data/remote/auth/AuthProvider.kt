package com.mikkelthygesen.billsplit.data.remote.auth

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.UserProfileChangeRequest
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.data.remote.exceptions.NetworkExceptions
import com.mikkelthygesen.billsplit.domain.models.Person
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProvider @Inject constructor() {

    var userState: Person? by mutableStateOf(null)
    var userLiveData = MutableLiveData<Person?>(null)

    private val firebase by lazy {
        FirebaseAuth.getInstance().apply {
            if (BuildConfig.FLAVOR == "emulator")
                useEmulator("10.0.2.2", 9099)
        }
    }

    var loggedInUser: Person? = null
        private set

    private val authListener = AuthStateListener {
        val fbUser = it.currentUser
        val userPerson = if (fbUser == null) null else Person(
            fbUser.uid,
            name = fbUser.displayName ?: "Splittsby User",
            pfpUrl = fbUser.photoUrl?.toString() ?: "",
            email = fbUser.email ?: ""
        )
        loggedInUser = userPerson
        userState = userPerson
        userLiveData.value = userPerson
    }

    fun onCreate() {
        firebase.addAuthStateListener(authListener)
    }

    fun onDestroy() {
        firebase.removeAuthStateListener(authListener)
    }

    fun signOut(){
        firebase.signOut()
        userState = null
        userLiveData.value = null
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