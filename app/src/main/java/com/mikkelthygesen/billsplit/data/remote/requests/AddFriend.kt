package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.FriendDTO
import com.mikkelthygesen.billsplit.data.remote.dto.PersonDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object AddFriend {

    @Serializable
    sealed class Request {

        @Serializable
        @SerialName("email")
        data class Email(val email: String) : Request()

        @Serializable
        @SerialName("userid")
        data class UserId(val friendId: String) : Request()
    }

    @Serializable
    data class Response(val friend: FriendDTO)
}