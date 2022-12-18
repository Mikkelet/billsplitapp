package com.mikkelthygesen.billsplit.data.local.room.converters

import androidx.room.TypeConverter
import com.mikkelthygesen.billsplit.data.local.room.models.DebtDb
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun listToJson(value: List<DebtDb>):String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(value: String): List<DebtDb> = Json.decodeFromString(value)
}