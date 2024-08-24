package com.san.busing.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.san.busing.data.entity.RouteRecentSearch

@Dao
interface RouteRecentSearchDao {
    @Query("SELECT * FROM routerecentsearch WHERE id = :id")
    suspend fun getRouteRecentSearch(id: Int): RouteRecentSearch?

    @Query("SELECT * FROM routerecentsearch")
    suspend fun getAllRouteRecentSearches(): List<RouteRecentSearch>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(routeRecentSearch: RouteRecentSearch)

    @Update
    suspend fun update(routeRecentSearch: RouteRecentSearch)

    @Delete
    suspend fun delete(routeRecentSearch: RouteRecentSearch)

    @Query("DELETE FROM routerecentsearch WHERE bookMark = 0")
    suspend fun deleteAllRouteRecentSearches()
}