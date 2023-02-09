package com.mikkelthygesen.billsplit.data.remote.dto

@kotlinx.serialization.Serializable
data class DebtDTO(
    val userId: String,
    val owes: Float
){
    fun toDebt() = userId to owes
}