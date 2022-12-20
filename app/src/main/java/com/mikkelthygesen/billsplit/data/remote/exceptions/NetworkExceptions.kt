package com.mikkelthygesen.billsplit.data.remote.exceptions

sealed class NetworkExceptions(message: String?) : Throwable(message) {
    object UserLoggedOutException : NetworkExceptions("No user found")
    object ForbiddenException : NetworkExceptions("User is unauthorized. Please log in again.")
    object NotFoundException : NetworkExceptions("The data was not found")
    data class GenericException(val error: Throwable) : NetworkExceptions(error.message)
}