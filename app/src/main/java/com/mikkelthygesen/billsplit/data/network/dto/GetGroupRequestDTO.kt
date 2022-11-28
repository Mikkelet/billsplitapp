package com.mikkelthygesen.billsplit.data.network.dto

@kotlinx.serialization.Serializable
data class GetGroupRequestDTO(
    val groupId: String
)