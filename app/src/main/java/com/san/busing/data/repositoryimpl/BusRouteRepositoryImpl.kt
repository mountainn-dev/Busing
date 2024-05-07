package com.san.busing.data.repositoryimpl

import com.san.busing.data.repository.BusRouteRepository
import com.san.busing.data.source.remote.retrofit.BusRouteService
import com.san.busing.domain.model.BusRouteModel
import retrofit2.Retrofit

class BusRouteRepositoryImpl(retrofit: Retrofit) : BusRouteRepository {
    private val service = retrofit.create(BusRouteService::class.java)
    override suspend fun getBusRoutes(): Result<List<BusRouteModel>> {
        val response = service.getBusRouteList()
        return try {
            Result.success(response.body()!!.map {
                it.toBusRouteModel()
            }.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}