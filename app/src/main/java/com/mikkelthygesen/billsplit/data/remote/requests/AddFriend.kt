package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.FriendDTO
import com.mikkelthygesen.billsplit.data.remote.dto.PersonDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object AddFriend {

    @Serializable
    sealed class Request {
        abstract val createdBy: String
        abstract val timeStamp: Long

        @Serializable
        @SerialName("email")
        data class Email(
            override val createdBy: String,
            override val timeStamp: Long,
            val email: String
        ) : Request()

        @Serializable
        @SerialName("userid")
        data class UserId(
            override val createdBy: String,
            override val timeStamp: Long,
            val user: PersonDTO
        ) : Request()
    }

    @Serializable
    data class Response(val friend: FriendDTO)
}