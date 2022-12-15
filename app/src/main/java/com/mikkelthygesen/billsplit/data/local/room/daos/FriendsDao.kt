package com.mikkelthygesen.billsplit.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.room.models.FriendDb

@Dao
interface FriendsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg friendDb: FriendDb)

    @Insert(onConflict = REPLACE)
    suspend fun insert(friendDb: List<FriendDb>)

    @Query("SELECT * FROM friends")
    suspend fun getFriends(): List<FriendDb>
}