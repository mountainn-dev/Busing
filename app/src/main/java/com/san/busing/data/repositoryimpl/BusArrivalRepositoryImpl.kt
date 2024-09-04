package com.san.busing.data.repositoryimpl

import android.util.Log
import com.san.busing.BuildConfig
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.repository.BusArrivalRepository
import com.san.busing.data.source.remote.retrofit.BusArrivalService
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusArrivalModels
import com.san.busing.data.Result

class BusArrivalRepositoryImpl(
    private val service: BusArrivalService
) : BusArrivalRepository {
    override suspend fun getBusArrivals(id: Id): Result<BusArrivalModels> {
        try {
            val response = service.getBusArrivalList(BuildConfig.API_KEY, id.get())
            return Result.success(BusArrivalModels(response.body()!!.get()))
        } catch (e: ServiceException.ResultException) {
            return Result.success(BusArrivalModels(listOf()))
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(BusArrivalModels(listOf()))
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_BUS_ARRIVAL_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }
}