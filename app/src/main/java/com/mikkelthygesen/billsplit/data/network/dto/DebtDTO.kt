package com.mikkelthygesen.billsplit.data.network.dto

import com.mikkelthygesen.billsplit.data.local.room.models.DebtDb

@kotlinx.serialization.Serializable
data class DebtDTO(
    val userId: String,
    val owes: Float
){
    fun toDebt() = userId to owes
    fun toDb() = DebtDb(userId, owes)
}