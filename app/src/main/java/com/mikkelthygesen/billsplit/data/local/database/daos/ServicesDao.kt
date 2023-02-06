package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.SubscriptionServiceDb

@Dao
interface ServicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg subscriptionServiceDb: SubscriptionServiceDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscriptionServiceDbs: List<SubscriptionServiceDb>)

    @Query("SELECT * FROM services WHERE :groupId == groupId")
    suspend fun getServices(groupId: String): List<SubscriptionServiceDb>

    @Query("SELECT * FROM services WHERE :groupId == id")
    suspend fun getService(groupId: String): SubscriptionServiceDb

    @Query("DELETE FROM services")
    suspend fun clearTable()
}