package com.mikkelthygesen.billsplit.data.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.mikkelthygesen.billsplit.models.Person


val authProvider = AuthProvider()

class AuthProvider {

    private val firebase by lazy { FirebaseAuth.getInstance() }

    var loggedInUser: Person? = null
        private set

    fun isUserLoggedIn(): Boolean {
        return loggedInUser != null
    }

    private val authListener = AuthStateListener {
        val user = it.currentUser
        if (user != null) {
            val person = Person(user.uid, name = "Mikkel")
            loggedInUser = person
        }
    }

    fun onCreate() {
        firebase.addAuthStateListener(authListener)
    }

    fun onDestroy() {
        firebase.removeAuthStateListener(authListener)
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
            val person = Person(user.uid, name = "Mikkel")
            loggedInUser = person
            onSuccess(person)
        }
    }
}