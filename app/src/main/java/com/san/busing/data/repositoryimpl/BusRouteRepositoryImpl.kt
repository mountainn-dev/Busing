package com.san.busing.data.repositoryimpl

import com.san.busing.BuildConfig
import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.source.remote.retrofit.BusRouteService
import com.san.busing.data.Result
import com.san.busing.data.type.Id
import com.san.busing.domain.model.BusRouteInfoModel
import com.san.busing.domain.model.BusRouteModel
import retrofit2.Retrofit

class BusRouteRepositoryImpl(retrofit: Retrofit) : BusRouteRepository {
    private val service = retrofit.create(BusRouteService::class.java)
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
}