package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.SubscriptionServiceDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ServicesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg subscriptionServiceDb: SubscriptionServiceDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscriptionServiceDbs: List<SubscriptionServiceDb>)

    @Query("SELECT * FROM services WHERE :groupId == groupId")
    fun getServicesFlow(groupId: String): Flow<List<SubscriptionServiceDb>>

    @Query("SELECT * FROM services WHERE :groupId == id")
    suspend fun getService(groupId: String): SubscriptionServiceDb

    @Query("DELETE FROM services")
    suspend fun clearTable()

    @Query("DELETE FROM services WHERE :groupId == groupid")
    suspend fun clearTable(groupId: String)
}