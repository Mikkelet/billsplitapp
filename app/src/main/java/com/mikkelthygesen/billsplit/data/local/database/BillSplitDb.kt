package com.mikkelthygesen.billsplit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikkelthygesen.billsplit.data.local.database.converters.Converters
import com.mikkelthygesen.billsplit.data.local.database.daos.FriendsDao
import com.mikkelthygesen.billsplit.data.local.database.daos.GroupsDao
import com.mikkelthygesen.billsplit.data.local.database.daos.PersonDao
import com.mikkelthygesen.billsplit.data.local.database.model.FriendDb
import com.mikkelthygesen.billsplit.data.local.database.model.GroupDb
import com.mikkelthygesen.billsplit.data.local.database.model.PersonDb

@Database(
    entities = [FriendDb::class, PersonDb::class, GroupDb::class],
    version = 5,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class
BillSplitDb : RoomDatabase() {

    abstract fun friendsDao(): FriendsDao
    abstract fun groupsDao(): GroupsDao
    abstract fun peopleDao(): PersonDao
}