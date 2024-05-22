package com.san.busing.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.san.busing.data.entity.Test
import com.san.busing.data.source.local.dao.TestDao

@Database(entities = [Test::class], version = 1)
abstract class TestDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao
}