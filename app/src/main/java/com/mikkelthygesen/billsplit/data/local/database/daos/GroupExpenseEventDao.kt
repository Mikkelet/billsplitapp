package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.*
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupExpenseEventDao {

    @Query("SELECT * FROM group_expenses WHERE :id == id")
    suspend fun getGroupExpense(id: String): GroupExpenseDb

    @Query("SELECT * FROM group_expenses WHERE :groupId == groupId")
    suspend fun getGroupExpenses(groupId: String): List<GroupExpenseDb>

    @Query("SELECT * FROM group_expenses WHERE :groupId == groupId")
    fun getGroupExpensesFlow(groupId: String): Flow<List<GroupExpenseDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupExpenseDb: GroupExpenseDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupExpenseDb: List<GroupExpenseDb>)

    @Delete
    suspend fun delete(groupExpenseDb: GroupExpenseDb)

    @Query("DELETE FROM group_expenses")
    suspend fun clearTable()

    @Query("DELETE FROM group_expenses WHERE :groupId == groupid")
    suspend fun clearTableForGroup(groupId: String)
}