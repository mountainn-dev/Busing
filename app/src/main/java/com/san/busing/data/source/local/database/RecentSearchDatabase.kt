package com.san.busing.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.san.busing.data.entity.route.RouteRecentSearch
import com.san.busing.data.entity.station.StationRecentSearch
import com.san.busing.data.source.local.dao.RouteRecentSearchDao
import com.san.busing.data.source.local.dao.StationRecentSearchDao

@Database(
    version = 1,
    entities = [RouteRecentSearch::class, StationRecentSearch::class],
    exportSchema = false
)
abstract class RecentSearchDatabase : RoomDatabase() {
    abstract fun routeRecentSearchDao(): RouteRecentSearchDao
    abstract fun stationRecentSearchDao(): StationRecentSearchDao
}