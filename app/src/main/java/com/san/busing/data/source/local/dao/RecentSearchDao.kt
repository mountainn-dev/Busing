package com.san.busing.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.san.busing.data.entity.RouteRecentSearch

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM routerecentsearch")
    fun getAll(): List<RouteRecentSearch>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(busRouteRecentSearch: RouteRecentSearch)

    @Update
    fun update(busRouteRecentSearch: RouteRecentSearch)

    @Delete
    fun delete(busRouteRecentSearch: RouteRecentSearch)

    @Delete
    fun deleteAll(busRouteRecentSearches: List<RouteRecentSearch>)
}