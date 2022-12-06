package com.mikkelthygesen.billsplit.models

import com.mikkelthygesen.billsplit.data.network.dto.FriendDTO
import com.mikkelthygesen.billsplit.data.network.dto.FriendStatusDTO

sealed class Friend {
    abstract val person: Person

    data class FriendRequestReceived(override val person: Person) : Friend()
    data class FriendRequestSent(override val person: Person) : Friend()
    data class FriendAccepted(override val person: Person) : Friend()

    companion object {
        fun fromDTO(friendDTO: FriendDTO): Friend {
            return when (friendDTO.status) {
                FriendStatusDTO.RequestAccepted -> FriendAccepted(friendDTO.friend.toPerson())
                FriendStatusDTO.RequestSent -> {
                    if (friendDTO.createdBy == friendDTO.friend.id) {
                        FriendRequestReceived(friendDTO.friend.toPerson())
                    } else {
                        FriendRequestSent(friendDTO.friend.toPerson())
                    }
                }
            }
        }
    }
}