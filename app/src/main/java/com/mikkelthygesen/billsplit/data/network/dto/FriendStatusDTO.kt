package com.mikkelthygesen.billsplit.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FriendStatusDTO {

    @Serializable
    @SerialName("pending")
    object RequestSent : FriendStatusDTO()

    @Serializable
    @SerialName("accepted")
    object RequestAccepted : FriendStatusDTO()
}