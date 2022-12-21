package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.PersonDb

@Dao
interface PersonDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg people: PersonDb)

    @Insert(onConflict = REPLACE)
    suspend fun insert(people: List<PersonDb>)

    @Query("SELECT * FROM people")
    suspend fun getPeople(): List<PersonDb>
}