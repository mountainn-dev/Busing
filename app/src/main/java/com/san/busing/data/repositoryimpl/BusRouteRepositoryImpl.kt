package com.san.busing.data.repositoryimpl

import android.content.Context
import androidx.room.Room
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.source.local.database.RecentSearchDatabase
import com.san.busing.data.source.remote.retrofit.BusRouteService
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.BusRouteModel
import com.san.busing.domain.model.BusRouteRecentSearchModel
import retrofit2.Retrofit

class BusRouteRepositoryImpl(
    private val retrofit: Retrofit,
    private val context: Context
) : BusRouteRepository {
    private val service = retrofit.create(BusRouteService::class.java)
    private val db = Room.databaseBuilder(
        context, RecentSearchDatabase::class.java, "recentSearch").build()

    override suspend fun getBusRoutes(keyword: String): Result<List<BusRouteModel>> {
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

    override fun getRecentSearch(): Result<List<BusRouteRecentSearchModel>> {
        try {
            return Result.success(db.recentSearchDao().getAll().map { it.toBusRouteRecentSearchModel() })
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun insertRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().insert(recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun deleteRecentSearch(recentSearchModel: BusRouteRecentSearchModel): Result<Boolean> {
        try {
            db.recentSearchDao().delete(recentSearchModel.toBusRouteRecentSearchEntity())
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }
}