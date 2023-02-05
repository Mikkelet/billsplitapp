package com.mikkelthygesen.billsplit.data.remote.requests

import com.mikkelthygesen.billsplit.data.remote.dto.ServiceDTO

object AddSubscriptionService {

    @kotlinx.serialization.Serializable
    data class Request(
        val groupId: String,
        val service: ServiceDTO
    )

    @kotlinx.serialization.Serializable
    data class Response(
        val service: ServiceDTO
    )
}