package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.FriendDb
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg friendDb: FriendDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(friendDb: List<FriendDb>)

    @Query("SELECT * FROM friends")
    suspend fun getFriends(): List<FriendDb>

    @Query("SELECT * FROM friends")
    fun getFriendsFlow(): Flow<List<FriendDb>>


    @Query("DELETE FROM friends")
    suspend fun clearTable()
}