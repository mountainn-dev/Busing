package com.san.busing.data.repositoryimpl

import android.util.Log
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.repository.BusLocationRepository
import com.san.busing.data.source.remote.retrofit.BusLocationService
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusModel
import com.san.busing.domain.model.BusModels

class BusLocationRepositoryImpl(
    private val service: BusLocationService
) : BusLocationRepository {
    override suspend fun getBusLocations(id: Id): Result<BusModels> {
        try {
            val response = service.getBusLocationList(BuildConfig.API_KEY, id.get())
            return Result.success(BusModels(response.body()!!.get()))
        } catch (e: ServiceException.ResultException) {
            return Result.success(BusModels(listOf()))
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(BusModels(listOf()))
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_BUS_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }
}