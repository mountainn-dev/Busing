package com.san.busing.data.repositoryimpl

import android.app.Activity
import android.content.Context
import androidx.room.Room
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.source.local.database.RecentSearchDatabase
import com.san.busing.data.source.remote.retrofit.BusRouteService
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.model.BusStationModel
import com.san.busing.domain.utils.Const
import retrofit2.Retrofit

class BusRouteRepositoryImpl(
    private val retrofit: Retrofit,
    private val context: Context
) : BusRouteRepository {
    private val service = retrofit.create(BusRouteService::class.java)
    private val db = Room.databaseBuilder(
        context, RecentSearchDatabase::class.java, "recentSearch").build()

    override suspend fun getBusRoute(id: Id): Result<BusRouteModel> {
        try {
            val response = service.getBusRouteInfoItem(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override suspend fun getBusRoutes(keyword: String): Result<List<BusRouteSearchResultModel>> {
        try {
            val response = service.getBusRouteList(BuildConfig.API_KEY, keyword)
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override suspend fun getBusStations(id: Id): Result<List<BusStationModel>> {
        try {
            val response = service.getBusStationList(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun getRecentSearch(): Result<List<BusRouteRecentSearchModel>> {
        try {
            return Result.success(db.recentSearchDao().getAll().map { it.toBusRouteRecentSearchModel() })
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun insertRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().insert(
                recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun updateRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().update(
                recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun deleteRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().delete(
                recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    /**
     * fun getRecentSearchIndex(context: Activty): Result<Int>
     *
     * 최근 검색 노선의 생성 고유 인덱스 호출 함수
     * preference.getInt() 에서 디폴트값을 설정하기 때문에 별도 예외처리를 진행하지 않는다.
     */
    override fun getRecentSearchIndex(context: Activity): Result<Long> {
        val preference = context.getPreferences(Context.MODE_PRIVATE)
        return Result.success(
            preference.getLong(BuildConfig.BUS_ROUTE_PREFERENCE_KEY, Const.ZERO.toLong()))
    }

    override fun updateRecentSearchIndex(context: Activity, newIdx: Long): Result<Boolean> {
        val preference = context.getPreferences(Context.MODE_PRIVATE)
        try {
            preference.edit().putLong(BuildConfig.BUS_ROUTE_PREFERENCE_KEY, newIdx).apply()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }
}