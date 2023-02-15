package com.mikkelthygesen.billsplit.data.remote.auth

import android.net.Uri
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.UserProfileChangeRequest
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.data.remote.exceptions.NetworkExceptions
import com.mikkelthygesen.billsplit.domain.models.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthProvider @Inject constructor() {

    private val _mutableUserFlow = MutableStateFlow<Person?>(null)
    val userStateFlow: StateFlow<Person?> = _mutableUserFlow
    val requireLoggedInUser: Person
        get() = userStateFlow.value ?: throw NetworkExceptions.UserLoggedOutException
    private val firebase by lazy {
        FirebaseAuth.getInstance().apply {
            if (BuildConfig.FLAVOR == "emulator")
                useEmulator("10.0.2.2", 9099)
        }
    }

    private val authListener = AuthStateListener {
        val fbUser = it.currentUser
        val userPerson = if (fbUser == null) null else Person(
            fbUser.uid,
            name = fbUser.displayName ?: "Splittsby User",
            pfpUrl = fbUser.photoUrl?.toString() ?: "",
            email = fbUser.email ?: ""
        )
        _mutableUserFlow.value = userPerson
    }

    fun onCreate() {
        firebase.addAuthStateListener(authListener)
    }

    fun onDestroy() {
        firebase.removeAuthStateListener(authListener)
    }

    fun signOut() {
        firebase.signOut()
        _mutableUserFlow.value = null
    }

    suspend fun updateUserName() {
        val currentUser = firebase.currentUser!!
        val request = UserProfileChangeRequest.Builder()
        request.displayName = requireLoggedInUser.nameState
        currentUser.updateProfile(request.build()).await()
        requireLoggedInUser.saveChanges()
    }

    @Suppress("KotlinConstantConditions")
    suspend fun updateProfilePicture(downloadUrl: Uri) {
        val currentUser = firebase.currentUser!!
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
            val person = Person(user.uid, name = user.displayName ?: "Splitsby user")
            _mutableUserFlow.value = person
        }
    }

    suspend fun getAuthToken(force: Boolean): String {
        val currentUser = firebase.currentUser
        val result = currentUser?.getIdToken(force)?.await()
        return result?.token!!
    }
}