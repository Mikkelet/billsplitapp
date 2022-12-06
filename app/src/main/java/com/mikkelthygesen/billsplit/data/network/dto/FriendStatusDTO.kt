package com.mikkelthygesen.billsplit.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FriendStatusDTO {

    @Serializable
    @SerialName("requestSent")
    object RequestSent : FriendStatusDTO()

    @Serializable
    @SerialName("requestAccepted")
    object RequestAccepted : FriendStatusDTO()

    @Serializable
    @SerialName("alreadyRequested")
    object AlreadyRequested : FriendStatusDTO()
}