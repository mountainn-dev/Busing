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
    @Query("SELECT * FROM routerecentsearch WHERE id = :id")
    suspend fun get(id: Int): RouteRecentSearch?

    @Query("SELECT * FROM routerecentsearch")
    suspend fun getAll(): List<RouteRecentSearch>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(busRouteRecentSearch: RouteRecentSearch)

    @Update
    suspend fun update(busRouteRecentSearch: RouteRecentSearch)

    @Delete
    suspend fun delete(busRouteRecentSearch: RouteRecentSearch)

    @Delete
    suspend fun deleteAll(busRouteRecentSearches: List<RouteRecentSearch>)
}