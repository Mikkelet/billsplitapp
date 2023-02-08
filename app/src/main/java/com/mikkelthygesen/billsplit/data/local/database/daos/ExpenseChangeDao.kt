package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.ExpenseChangeDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseChangeDao {

    @Query("SELECT * FROM expense_changes WHERE :groupId == groupId")
    suspend fun getExpenseChanges(groupId: String): List<ExpenseChangeDb>

    @Query("SELECT * FROM expense_changes WHERE :groupId == groupId")
    fun getExpenseChangesFlow(groupId: String): Flow<List<ExpenseChangeDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseChange: ExpenseChangeDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expenseChanges: List<ExpenseChangeDb>)

    @Query("DELETE FROM expense_changes")
    suspend fun clearTable()

    @Query("DELETE FROM expense_changes WHERE :groupId == groupid")
    suspend fun clearTableForGroup(groupId: String)


}