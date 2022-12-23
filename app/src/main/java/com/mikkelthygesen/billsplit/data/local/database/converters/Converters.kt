package com.mikkelthygesen.billsplit.data.local.database.converters

import androidx.room.TypeConverter
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.DebtDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.IndividualExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.embedded.PersonDb
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun debtToJson(value: List<DebtDb>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToDebt(value: String): List<DebtDb> = Json.decodeFromString(value)

    @TypeConverter
    fun individualExpenseToJson(value: List<IndividualExpenseDb>): String =
        Json.encodeToString(value)

    @TypeConverter
    fun jsonToIndividualExpense(value: String): List<IndividualExpenseDb> =
        Json.decodeFromString(value)

    @TypeConverter
    fun peopleToJson(value: List<PersonDb>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToPeople(value: String): List<PersonDb> = Json.decodeFromString(value)

    @TypeConverter
    fun personToJson(value: PersonDb): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToPerson(value: String): PersonDb = Json.decodeFromString(value)
}
