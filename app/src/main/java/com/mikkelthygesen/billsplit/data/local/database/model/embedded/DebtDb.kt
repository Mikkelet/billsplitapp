package com.mikkelthygesen.billsplit.data.local.database.model.embedded

import com.mikkelthygesen.billsplit.data.remote.dto.DebtDTO

@kotlinx.serialization.Serializable
data class DebtDb(
    val userId: String,
    val owes: Float
){
    constructor(debtDTO: DebtDTO):this(
        userId = debtDTO.userId,
        owes = debtDTO.owes
    )

    fun toDebt() = userId to owes
}