package com.mikkelthygesen.billsplit.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkelthygesen.billsplit.data.local.database.model.ExpenseChangeDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupExpenseDb
import com.mikkelthygesen.billsplit.data.local.database.model.PaymentDb

@Dao
interface EventsDao {

    // Group expenses
    @Query("SELECT * FROM group_expenses WHERE :groupId == groupId")
    suspend fun getGroupExpenses(groupId: String): List<GroupExpenseDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupExpense(groupExpenseDb: GroupExpenseDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupExpenses(groupExpenseDb: List<GroupExpenseDb>)

    // Payment
    @Query("SELECT * FROM payments WHERE :groupId == groupId")
    suspend fun getPayments(groupId: String): List<PaymentDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<PaymentDb>)

    // Expense Change
    @Query("SELECT * FROM expense_changes WHERE :groupId == groupId")
    suspend fun getExpenseChanges(groupId: String): List<ExpenseChangeDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseChange(expenseChange: ExpenseChangeDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseChanges(expenseChanges: List<ExpenseChangeDb>)


}