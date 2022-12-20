package com.mikkelthygesen.billsplit.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class FriendStatusDTO {

    @Serializable
    @SerialName("pending")
    object RequestSent : FriendStatusDTO() {
        override fun toString(): String {
            return STATUS_PENDING
        }
    }

    @Serializable
    @SerialName("accepted")
    object RequestAccepted : FriendStatusDTO() {
        override fun toString(): String {
            return STATUS_ACCEPTED
        }
    }

    companion object {
        const val STATUS_PENDING = "pending"
        const val STATUS_ACCEPTED = "accepted"
    }
}