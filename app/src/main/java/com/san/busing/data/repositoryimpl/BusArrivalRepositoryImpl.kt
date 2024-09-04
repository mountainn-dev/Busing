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

}