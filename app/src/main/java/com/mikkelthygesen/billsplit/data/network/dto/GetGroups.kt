package com.mikkelthygesen.billsplit.data.network.dto

object GetGroups {
    @kotlinx.serialization.Serializable
    data class Request(
        val userId: String
    )

    @kotlinx.serialization.Serializable
    data class Response(
        val groups: List<GroupDTO>
    )
}