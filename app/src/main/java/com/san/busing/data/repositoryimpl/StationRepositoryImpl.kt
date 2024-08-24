package com.san.busing.data.repositoryimpl

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.repository.StationRepository
import com.san.busing.data.source.local.database.RecentSearchDatabase
import com.san.busing.data.source.remote.retrofit.StationService
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.StationRecentSearchModel
import com.san.busing.domain.model.StationSummaryModel

class StationRepositoryImpl(
    private val service: StationService,
    private val context: Context
) : StationRepository {
    private val db = Room.databaseBuilder(
        this.context, RecentSearchDatabase::class.java, "recentSearch").build()

    override suspend fun getStations(keyword: String): Result<List<StationSummaryModel>> {
        try {
            val response = service.getBusStationList(BuildConfig.API_KEY, keyword)
            return Result.success(response.body()!!.get())
        } catch (e: ServiceException.ResultException) {
            return Result.success(listOf())
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(listOf())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_STATION_SUMMARY_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRecentSearch(id: Id): Result<StationRecentSearchModel> {
        try {
            db.stationRecentSearchDao().getStationRecentSearch(id.get())?.let {
                return Result.success(it.toStationRecentSearchModel())
            }
            return Result.error(NoSuchElementException(""))
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getAllRecentSearch(): Result<List<StationRecentSearchModel>> {
        try {
            return Result.success(db.stationRecentSearchDao().getAllStationRecentSearches().map { it.toStationRecentSearchModel() })
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun insertRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean> {
        try {
            db.stationRecentSearchDao().insert(
                recentSearchModel.toStationRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun updateRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean> {
        try {
            db.stationRecentSearchDao().update(
                recentSearchModel.toStationRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean> {
        try {
            db.stationRecentSearchDao().delete(
                recentSearchModel.toStationRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteAllRecentSearch(): Result<Boolean> {
        try {
            db.stationRecentSearchDao().deleteAllStationRecentSearches()
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override fun getRecentSearchIndex(activity: Activity): Result<Long> {
        val preference = activity.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return Result.success(
            preference.getLong(BuildConfig.STATION_PREFERENCE_KEY, DEFAULT_INDEX)
        )
    }

    override fun updateRecentSearchIndex(activity: Activity, newIdx: Long): Result<Boolean> {
        val preference = activity.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        try {
            preference.edit().putLong(BuildConfig.STATION_PREFERENCE_KEY, newIdx).apply()
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    companion object {
        private const val DEFAULT_INDEX: Long = 0
    }
}