package com.san.busing.data.repositoryimpl

import android.util.Log
import com.san.busing.BuildConfig
import com.san.busing.data.Result
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.repository.StationRepository
import com.san.busing.data.source.remote.retrofit.StationService
import com.san.busing.domain.model.StationSummaryModel

class StationRepositoryImpl(
    private val service: StationService
) : StationRepository {
    override suspend fun getStations(keyword: String): Result<List<StationSummaryModel>> {
        try {
            val response = service.getBusStationList(BuildConfig.API_KEY, keyword)
            return Result.success(response.body()!!.get())
        } catch (e: ServiceException.ResultException) {
            return Result.success(listOf())
        } catch (e: ServiceException.OptionalParameterException) {
            return Result.success(listOf())
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_STATION_SUMMARY_EXCEPTION, e.message ?: e.toString())
            return Result.error(e)
        }
    }
}