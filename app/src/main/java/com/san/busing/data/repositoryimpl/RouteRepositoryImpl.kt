package com.san.busing.data.repositoryimpl

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.repository.RouteRepository
import com.san.busing.data.source.local.database.RecentSearchDatabase
import com.san.busing.data.source.remote.retrofit.BusLocationService
import com.san.busing.data.source.remote.retrofit.RouteService
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.BusModels
import com.san.busing.domain.modelimpl.RouteInfoModel
import com.san.busing.domain.modelimpl.RouteModels
import com.san.busing.domain.modelimpl.RouteRecentSearchModel
import com.san.busing.domain.modelimpl.RouteRecentSearchModels
import com.san.busing.domain.modelimpl.StationModels

class RouteRepositoryImpl(
    private val routeService: RouteService,
    private val busLocationService: BusLocationService,
    private val context: Context
) : RouteRepository {
    private val db = Room.databaseBuilder(
        this.context, RecentSearchDatabase::class.java, "recentSearch").build()

    override suspend fun getRouteInfo(id: Id): Result<RouteInfoModel> {
        try {
            val response = routeService.getBusRouteInfoItem(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_INFO_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRoutes(keyword: String): Result<RouteModels> {
        try {
            val response = routeService.getBusRouteList(BuildConfig.API_KEY, keyword)
            val routes = response.body()!!.get()
            routes.sort()
            return Result.success(routes)
        } catch (e: ServiceException.ResultException) {
            return Result.success(RouteModels.instance())
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(RouteModels.instance())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_SUMMARY_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRouteStations(id: Id): Result<StationModels> {
        try {
            val response = routeService.getBusStationList(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: ServiceException.ResultException) {
            return Result.success(StationModels.instance())
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(StationModels.instance())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_STATION_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getBusLocations(id: Id): Result<BusModels> {
        try {
            val response = busLocationService.getBusLocationList(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: ServiceException.ResultException) {
            return Result.success(BusModels.instance())
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(BusModels.instance())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_BUS_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRecentSearch(id: Id): Result<RouteRecentSearchModel> {
        try {
            db.routeRecentSearchDao().getRouteRecentSearch(id.get())?.let {
                return Result.success(it.toRouteRecentSearchModel())
            }
            return Result.error(NoSuchElementException(""))
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getAllRecentSearch(): Result<RouteRecentSearchModels> {
        try {
            val recentSearchModels = db.routeRecentSearchDao().getAllRouteRecentSearches().map { it.toRouteRecentSearchModel() }
            return Result.success(RouteRecentSearchModels(recentSearchModels))
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun insertRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean> {
        try {
            db.routeRecentSearchDao().insert(
                recentSearchModel.toRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun updateRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean> {
        try {
            db.routeRecentSearchDao().update(
                recentSearchModel.toRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean> {
        try {
            db.routeRecentSearchDao().delete(
                recentSearchModel.toRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteAllRecentSearch(): Result<Boolean> {
        try {
            db.routeRecentSearchDao().deleteAllRouteRecentSearches()
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.toString())
            return Result.error(e)
        }
    }

    /**
     * fun getRecentSearchIndex(context: Activity): Result<Int>
     *
     * 최근 검색 노선의 생성 고유 인덱스 호출 함수
     * preference.getLong() 에서 디폴트값을 설정하기 때문에 별도 예외처리를 진행하지 않는다.
     */
    override fun getRecentSearchIndex(activity: Activity): Result<Long> {
        val preference = activity.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return Result.success(
            preference.getLong(BuildConfig.ROUTE_PREFERENCE_KEY, DEFAULT_INDEX)
        )
    }

    override fun updateRecentSearchIndex(activity: Activity, newIdx: Long): Result<Boolean> {
        val preference = activity.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        try {
            preference.edit().putLong(BuildConfig.ROUTE_PREFERENCE_KEY, newIdx).apply()
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