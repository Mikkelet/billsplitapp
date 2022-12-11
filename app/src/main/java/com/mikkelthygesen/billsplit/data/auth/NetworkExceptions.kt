package com.mikkelthygesen.billsplit.data.auth

sealed class NetworkExceptions : Exception() {
    class UserLoggedOut : Exception()
}