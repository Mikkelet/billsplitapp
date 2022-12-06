package com.mikkelthygesen.billsplit.data.network.requests

import com.mikkelthygesen.billsplit.data.network.dto.FriendDTO

object GetFriends {

    @kotlinx.serialization.Serializable
    data class Request(val uid: String)

    @kotlinx.serialization.Serializable
    data class Response(val friends: List<FriendDTO>)
}