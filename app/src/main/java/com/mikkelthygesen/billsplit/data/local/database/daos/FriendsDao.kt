package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.FriendDb

@Dao
interface FriendsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg friendDb: FriendDb)

    @Insert(onConflict = REPLACE)
    suspend fun insert(friendDb: List<FriendDb>)

    @Query("SELECT * FROM friends")
    suspend fun getFriends(): List<FriendDb>
}