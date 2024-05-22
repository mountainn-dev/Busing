package com.san.busing.data.repositoryimpl

import android.content.Context
import androidx.room.Room
import com.san.busing.BuildConfig
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.source.remote.retrofit.BusRouteService
import com.san.busing.data.Result
import com.san.busing.data.entity.Test
import com.san.busing.data.source.local.database.TestDatabase
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.BusRouteModel
import retrofit2.Retrofit

class BusRouteRepositoryImpl(
    private val retrofit: Retrofit,
    private val context: Context
) : BusRouteRepository {
    private val service = retrofit.create(BusRouteService::class.java)
    private val db = Room.databaseBuilder(
        context, TestDatabase::class.java, "recentSearch").build()

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

    override fun getTest(): Result<List<Test>> {
        try {
            return Result.success(db.testDao().getAll())
        } catch (e: Exception) {
            return Result.error(e)
        }
    }

    override fun insertRecentSearch(test: Test): Result<Boolean> {
        try {
            db.testDao().insert(test)
            return Result.success(true)
        } catch (e: Exception) {
            return Result.error(e)
        }
    }
}