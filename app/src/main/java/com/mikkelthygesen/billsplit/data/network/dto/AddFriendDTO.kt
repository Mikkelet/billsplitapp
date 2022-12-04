package com.mikkelthygesen.billsplit.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object AddFriendDTO {

    @Serializable
    data class Request(
        val createdBy: String,
        val sentTo: String,
        val timeStamp: Long
    )

    @Serializable
    sealed class Response {

        @Serializable
        @SerialName("requestSent")
        object RequestSent : Response()

        @Serializable
        @SerialName("requestAccepted")
        object RequestAccepted : Response()

        @Serializable
        @SerialName("alreadyRequested")
        object AlreadyRequested : Response()
    }
}