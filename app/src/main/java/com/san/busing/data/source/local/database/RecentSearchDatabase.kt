package com.san.busing.data.source.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.san.busing.data.entity.BusRouteRecentSearch
import com.san.busing.data.source.local.dao.RecentSearchDao

@Database(
    version = 1,
    entities = [BusRouteRecentSearch::class],
)
abstract class RecentSearchDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}