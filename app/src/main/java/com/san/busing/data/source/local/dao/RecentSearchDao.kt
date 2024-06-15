package com.san.busing.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.san.busing.data.entity.BusRouteRecentSearch

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM busrouterecentsearch")
    fun getAll(): List<BusRouteRecentSearch>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(busRouteRecentSearch: BusRouteRecentSearch)

    @Update
    fun update(busRouteRecentSearch: BusRouteRecentSearch)

    @Delete
    fun delete(busRouteRecentSearch: BusRouteRecentSearch)
}