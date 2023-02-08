package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.PaymentDb
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentEventDao {

    // Payment
    @Query("SELECT * FROM payments WHERE :groupId == groupId")
    suspend fun getPayments(groupId: String): List<PaymentDb>

    @Query("SELECT * FROM payments WHERE :groupId == groupId")
    fun getPaymentsFlow(groupId: String): Flow<List<PaymentDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PaymentDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payments: List<PaymentDb>)

    @Query("DELETE FROM payments")
    suspend fun clearTable()

    @Query("DELETE FROM payments WHERE :groupId == groupid")
    suspend fun clearTableForGroup(groupId: String)
}