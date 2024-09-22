package com.san.busing.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.san.busing.data.entity.station.StationRecentSearch

@Dao
interface StationRecentSearchDao {
    @Query("SELECT * FROM stationrecentsearch WHERE id = :id")
    suspend fun getStationRecentSearch(id: Int): StationRecentSearch?

    @Query("SELECT * FROM stationrecentsearch")
    suspend fun getAllStationRecentSearches(): List<StationRecentSearch>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stationRecentSearch: StationRecentSearch)

    @Update
    suspend fun update(stationRecentSearch: StationRecentSearch)

    @Delete
    suspend fun delete(stationRecentSearch: StationRecentSearch)

    @Query("DELETE FROM stationrecentsearch WHERE bookMark = 0")
    suspend fun deleteAllStationRecentSearches()
}