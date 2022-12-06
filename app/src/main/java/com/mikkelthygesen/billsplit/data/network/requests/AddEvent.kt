package com.mikkelthygesen.billsplit.data.network.requests

import com.mikkelthygesen.billsplit.data.network.dto.EventDTO

object AddEvent {

    @kotlinx.serialization.Serializable
    data class Request(
        val groupId: String,
        val event: EventDTO
    )

    @kotlinx.serialization.Serializable
    data class Response(
        val event: EventDTO
    )
}