package com.mikkelthygesen.billsplit.data.auth

sealed class NetworkExceptions : Exception() {
    object UserLoggedOut : Exception()
}