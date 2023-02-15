package com.mikkelthygesen.billsplit.data.local.database.model.embedded

import kotlinx.serialization.Serializable

@Serializable
data class LatestEventDb(
    val id: String?,
    val type: String?,
)