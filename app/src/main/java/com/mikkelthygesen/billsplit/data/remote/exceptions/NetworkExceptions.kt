package com.mikkelthygesen.billsplit.data.remote.exceptions

sealed class NetworkExceptions(message: String?) : Throwable(message) {
    object UserLoggedOutException : NetworkExceptions("User not logged in")
    object ForbiddenException : NetworkExceptions("User is unauthorized. Please log in again.")
    object NotFoundException : NetworkExceptions("Not found")
    data class GenericException(val error: Throwable) : NetworkExceptions(error.message)
}