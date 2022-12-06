package com.mikkelthygesen.billsplit.data.network.requests

import com.mikkelthygesen.billsplit.data.network.dto.GroupDTO

object AddGroup {

    @kotlinx.serialization.Serializable
    data class Request(val group: GroupDTO)

    @kotlinx.serialization.Serializable
    data class Response(val group: GroupDTO)
}