package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg groups: GroupDb)

    @Insert(onConflict = REPLACE)
    suspend fun insert(groups: List<GroupDb>)

    @Query("SELECT * FROM groups")
    suspend fun getGroups() : List<GroupDb>

    @Query("SELECT * FROM groups")
    fun getGroupsFlow() : Flow<List<GroupDb>>
}