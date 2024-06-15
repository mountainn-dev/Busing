package com.san.busing.data.repositoryimpl

import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.repository.BusLocationRepository
import com.san.busing.data.source.remote.retrofit.BusLocationService
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel
import retrofit2.Retrofit

class BusLocationRepositoryImpl(
    private val retrofit: Retrofit
) : BusLocationRepository {
    private val service = retrofit.create(BusLocationService::class.java)
    override suspend fun getBusLocations(id: Id): Result<List<BusModel>> {
        try {
            val response = service.getBusLocationList(BuildConfig.API_KEY, id.get())
            return Result.success(response.body()!!.get())
        } catch (e: ServiceException.NoResultException) {
            return Result.success(listOf())
        } catch (e: Exception) {
            return Result.error(e)
        }
    }
}