package com.mikkelthygesen.billsplit.data.auth

sealed class NetworkExceptions : Throwable() {
    object UserLoggedOut : NetworkExceptions()
}