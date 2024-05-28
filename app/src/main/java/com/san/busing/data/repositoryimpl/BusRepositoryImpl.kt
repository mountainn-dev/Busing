package com.san.busing.data.repositoryimpl

import android.app.Activity
import android.content.Context
import androidx.room.Room
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.repository.BusRepository
import com.san.busing.data.source.local.database.RecentSearchDatabase
import com.san.busing.data.source.remote.retrofit.BusRouteService
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.RecentSearchModel
import com.san.busing.domain.modelimpl.BusRouteSearchResultModelImpl
import com.san.busing.domain.modelimpl.BusRouteRecentSearchModelImpl
import com.san.busing.domain.utils.Const
import retrofit2.Retrofit

class BusRepositoryImpl(
    private val retrofit: Retrofit,
    private val context: Context
) : BusRepository {
    private val service = retrofit.create(BusRouteService::class.java)
    private val db = Room.databaseBuilder(
        context, RecentSearchDatabase::class.java, "recentSearch").build()

    override suspend fun getBusRoutes(keyword: String): Result<List<BusRouteSearchResultModelImpl>> {
        try {
            val response = service.getBusRouteList(BuildConfig.API_KEY, keyword)
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override suspend fun getBusRouteInfo(id: Id): Result<BusRouteInfoModel> {
        try {
            val response = service.getBusRouteInfoItem(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun getRecentSearch(): Result<List<BusRouteRecentSearchModelImpl>> {
        try {
            return Result.success(db.recentSearchDao().getAll().map { it.toBusRouteRecentSearchModel() })
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    /**
     * fun getRecentSearchIndex(context: Activty): Result<Int>
     *
     * 최근 검색 아이템의 생성 고유 인덱스 호출 함수
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

    override fun insertRecentSearch(recentSearchModel: RecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().insert(
                (recentSearchModel as BusRouteRecentSearchModelImpl).toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun deleteRecentSearch(recentSearchModel: RecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().delete(
                (recentSearchModel as BusRouteRecentSearchModelImpl).toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }
}