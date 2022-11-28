package com.mikkelthygesen.billsplit.data.network.dto


@kotlinx.serialization.Serializable
data class AddGroupRequestDTO(
    val group: GroupDTO
)