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
import com.san.busing.data.source.remote.retrofit.RouteService
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteInfoModel
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.utils.Const
import retrofit2.Retrofit
import java.net.SocketTimeoutException

class RouteRepositoryImpl(
    private val retrofit: Retrofit,
    private val context: Context
) : RouteRepository {
    private val service = retrofit.create(RouteService::class.java)
    private val db = Room.databaseBuilder(
        context, RecentSearchDatabase::class.java, "recentSearch").build()

    override suspend fun getRouteInfo(id: Id): Result<RouteInfoModel> {
        try {
            val response = service.getBusRouteInfoItem(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_INFO_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRoutes(keyword: String): Result<List<RouteSummaryModel>> {
        try {
            val response = service.getBusRouteList(BuildConfig.API_KEY, keyword)
            return Result.success(response.body()!!.get())
        } catch (e: ServiceException.ResultException) {   // 검색 결과 없음
            return Result.success(listOf())
        } catch (e: ServiceException.ParameterException) {   // 키워드 조건 충족 x
            return Result.success(listOf())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_SUMMARY_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRouteStations(id: Id): Result<List<RouteStationModel>> {
        try {
            val response = service.getBusStationList(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_STATION_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRecentSearch(id: Id): Result<RouteRecentSearchModel> {
        try {
            db.recentSearchDao().get(id.get())?.let {
                return Result.success(it.toRouteRecentSearchModel())
            }
            return Result.error(NoSuchElementException(""))
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun getRecentSearches(): Result<List<RouteRecentSearchModel>> {
        try {
            return Result.success(db.recentSearchDao().getAll().map { it.toRouteRecentSearchModel() })
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun insertRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().insert(
                recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun updateRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().update(
                recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteRecentSearch(recentSearchModel: RouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().delete(
                recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    override suspend fun deleteAllRecentSearch(recentSearchModels: List<RouteRecentSearchModel>): Result<Boolean> {
        try {
            db.recentSearchDao().deleteAll(
                recentSearchModels.map { it.toBusRouteRecentSearchEntity() }.toList())
            return Result.success(true)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ROUTE_RECENT_SEARCH_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }

    /**
     * fun getRecentSearchIndex(context: Activity): Result<Int>
     *
     * 최근 검색 노선의 생성 고유 인덱스 호출 함수
     * preference.getLong() 에서 디폴트값을 설정하기 때문에 별도 예외처리를 진행하지 않는다.
     */
    override fun getRecentSearchIndex(context: Activity): Result<Long> {
        val preference = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return Result.success(
            preference.getLong(BuildConfig.BUS_ROUTE_PREFERENCE_KEY, DEFAULT_INDEX)
        )
    }

    override fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean> {
        val preference = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        try {
            preference.edit().putLong(BuildConfig.BUS_ROUTE_PREFERENCE_KEY, newIdx).apply()
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