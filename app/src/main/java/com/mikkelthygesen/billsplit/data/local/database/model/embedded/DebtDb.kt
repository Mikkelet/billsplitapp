package com.mikkelthygesen.billsplit.data.local.database.model.embedded

@kotlinx.serialization.Serializable
data class DebtDb(
    val userId: String,
    val owes: Float
){
    fun toDebt() = userId to owes
}