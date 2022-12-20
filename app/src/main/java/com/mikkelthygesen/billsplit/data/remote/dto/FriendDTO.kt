package com.mikkelthygesen.billsplit.data.remote.dto

import com.mikkelthygesen.billsplit.data.local.database.model.FriendDb

@kotlinx.serialization.Serializable
data class FriendDTO(
    val id: String,
    val timeStamp: Long,
    val createdBy: String,
    val status: FriendStatusDTO,
    val friend: PersonDTO,
) {

    fun toDB() = FriendDb(
        id = id,
        timeStamp = timeStamp,
        createdBy = createdBy,
        status = status.toString(),
        friend = friend.toDB(),
    )
}