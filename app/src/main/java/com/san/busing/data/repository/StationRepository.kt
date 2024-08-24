package com.san.busing.data.repository

import android.app.Activity
import com.san.busing.data.Result
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.StationRecentSearchModel
import com.san.busing.domain.model.StationSummaryModel

interface StationRepository {
    suspend fun getStations(keyword: String): Result<List<StationSummaryModel>>
    suspend fun getRecentSearch(id: Id): Result<StationRecentSearchModel>
    suspend fun getAllRecentSearch(): Result<List<StationRecentSearchModel>>
    suspend fun insertRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>
    suspend fun updateRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>
    suspend fun deleteRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean>
    suspend fun deleteAllRecentSearch(): Result<Boolean>
    fun getRecentSearchIndex(activity: Activity): Result<Long>
    fun updateRecentSearchIndex(activity: Activity, newIdx: Long): Result<Boolean>
}