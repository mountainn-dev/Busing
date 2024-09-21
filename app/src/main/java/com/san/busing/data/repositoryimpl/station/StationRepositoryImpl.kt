package com.san.busing.data.repositoryimpl.station

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.repository.station.StationRepository
import com.san.busing.data.source.local.database.RecentSearchDatabase
import com.san.busing.data.source.remote.retrofit.station.BusArrivalService
import com.san.busing.data.source.remote.retrofit.station.StationService
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.modelimpl.station.BusArrivalModel
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.modelimpl.station.StationRecentSearchModel
import com.san.busing.domain.modelimpl.station.StationRecentSearchModels

class StationRepositoryImpl(
    private val stationService: StationService,
    private val busArrivalService: BusArrivalService,
    private val context: Context
) : StationRepository {
    private val db = Room.databaseBuilder(
        this.context, RecentSearchDatabase::class.java, "recentSearch"
    ).build()

    override suspend fun getStations(keyword: String): Result<StationModels> {
        try {
            val response = stationService.getBusStationList(BuildConfig.API_KEY, keyword)
            val stations = response.body()!!.get()
            stations.sort()
            return Result.success(stations)
        } catch (e: ServiceException.ResultException) {
            return Result.success(StationModels.instance())
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(StationModels.instance())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_STATION_SUMMARY_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getStationViaRoutes(id: Id): Result<RouteModels> {
        try {
            val response = stationService.getBusStationViaRouteList(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: ServiceException.ResultException) {
            return Result.success(RouteModels.instance())
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(RouteModels.instance())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_STATION_VIA_ROUTE_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    /**
     * getBusArrival
     *
     * 버스 도착 정보 호출 api
     * ServiceException$ResultException == "버스 도착 정보 없음"
     */
    override suspend fun getBusArrival(
        stationId: Id,
        routeId: Id,
        stationSeq: Int
    ): Result<BusArrivalModel> {
        try {
            val response = busArrivalService.getBusArrivalItem(
                BuildConfig.API_KEY, stationId.get(), routeId.get(), stationSeq
            )
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_BUS_ARRIVAL_EXCEPTION, e.toString())
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
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getAllRecentSearch(): Result<StationRecentSearchModels> {
        try {
            val recentSearchModels = db.stationRecentSearchDao().getAllStationRecentSearches().map { it.toStationRecentSearchModel() }
            return Result.success(StationRecentSearchModels(recentSearchModels))
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun insertRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean> {
        try {
            db.stationRecentSearchDao().insert(
                recentSearchModel.toStationRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun updateRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean> {
        try {
            db.stationRecentSearchDao().update(
                recentSearchModel.toStationRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteRecentSearch(recentSearchModel: StationRecentSearchModel): Result<Boolean> {
        try {
            db.stationRecentSearchDao().delete(
                recentSearchModel.toStationRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteAllRecentSearch(): Result<Boolean> {
        try {
            db.stationRecentSearchDao().deleteAllStationRecentSearches()
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override fun getRecentSearchIndex(activity: Activity): Result<Long> {
        val preference = activity.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        )
        return Result.success(
            preference.getLong(BuildConfig.STATION_PREFERENCE_KEY, DEFAULT_INDEX)
        )
    }

    override fun updateRecentSearchIndex(activity: Activity, newIdx: Long): Result<Boolean> {
        val preference = activity.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        )
        try {
            preference.edit().putLong(BuildConfig.STATION_PREFERENCE_KEY, newIdx).apply()
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    companion object {
        private const val DEFAULT_INDEX: Long = 0
    }
}