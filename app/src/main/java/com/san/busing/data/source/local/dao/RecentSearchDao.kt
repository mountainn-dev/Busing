package com.san.busing.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.san.busing.data.entity.BusRouteRecentSearch

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM busrouterecentsearch")
    fun getAll(): List<BusRouteRecentSearch>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(busRouteRecentSearch: BusRouteRecentSearch)
}