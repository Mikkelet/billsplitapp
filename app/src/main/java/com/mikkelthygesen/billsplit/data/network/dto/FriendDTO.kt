package com.mikkelthygesen.billsplit.data.network.dto

@kotlinx.serialization.Serializable
data class FriendDTO(
    val id: String,
    val timeStamp: Long,
    val createdBy: String,
    val status: FriendStatusDTO,
    val friend: PersonDTO,
)