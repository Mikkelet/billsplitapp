package com.mikkelthygesen.billsplit.data.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.UserProfileChangeRequest
import com.mikkelthygesen.billsplit.BuildConfig
import com.mikkelthygesen.billsplit.models.Person


val authProvider = AuthProvider()

class AuthProvider {

    private val firebase by lazy {
        FirebaseAuth.getInstance().apply {
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
            val person = Person(user.uid, name = user.displayName ?: "No name")
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

    fun updateUserName(name: String, onSuccess: (Void) -> Unit, onFailure: (Throwable) -> Unit) {
        firebase.currentUser?.updateProfile(UserProfileChangeRequest.Builder().apply {
            displayName = name
        }.build())
            ?.addOnFailureListener(onFailure)
            ?.addOnSuccessListener(onSuccess)
    }

    fun signUpWithEmail(
        email: String,
        password: String,
        onSuccess: (Person) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        firebase.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener { onFailure(it) }
            .addOnSuccessListener { onSignIn(it, onSuccess) }
    }

    fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: (Person) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        firebase.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSignIn(it, onSuccess) }
            .addOnFailureListener { onFailure(it) }
    }

    private fun onSignIn(authResult: AuthResult, onSuccess: (Person) -> Unit) {
        val user = authResult.user
        if (user != null) {
            val person = Person(user.uid, name = user.displayName ?: "Unknown user")
            loggedInUser = person
            onSuccess(person)
        }
    }
}