package com.san.busing.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.san.busing.data.entity.RouteRecentSearch
import com.san.busing.data.source.local.dao.RecentSearchDao

@Database(
    version = 1,
    entities = [RouteRecentSearch::class],
)
abstract class RecentSearchDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}