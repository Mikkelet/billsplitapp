package com.mikkelthygesen.billsplit.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import com.mikkelthygesen.billsplit.data.local.room.daos.FriendsDao
import com.mikkelthygesen.billsplit.data.local.room.daos.GroupsDao
import com.mikkelthygesen.billsplit.data.local.room.daos.PersonDao
import com.mikkelthygesen.billsplit.data.local.room.models.FriendDb
import com.mikkelthygesen.billsplit.data.local.room.models.GroupDb
import com.mikkelthygesen.billsplit.data.local.room.models.PersonDb

@Database(
    entities = [FriendDb::class, PersonDb::class, GroupDb::class],
    version = 2,
    exportSchema = false,

)
abstract class BillSplitDb : RoomDatabase() {

    abstract fun friendsDao(): FriendsDao
    abstract fun groupsDao(): GroupsDao
    abstract fun peopleDao(): PersonDao
}