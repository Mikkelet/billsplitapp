package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb

@Dao
interface GroupExpenseEventDao {

    @Query("SELECT * FROM group_expenses WHERE :groupId == groupId")
    suspend fun getAllForGroup(groupId: String): List<GroupExpenseDb>

    @Query("SELECT * FROM group_expenses WHERE id == :expenseId")
    suspend fun getGroupExpense(expenseId: String): GroupExpenseDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupExpenseDb: GroupExpenseDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupExpenseDb: List<GroupExpenseDb>)

    @Query("DELETE FROM group_expenses")
    suspend fun clearTable()

    @Query("DELETE FROM group_expenses WHERE :groupId == groupid")
    suspend fun clearTableForGroup(groupId: String)
}