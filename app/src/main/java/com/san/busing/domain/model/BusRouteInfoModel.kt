package com.san.busing.domain.model

import com.san.busing.data.type.Id
import com.san.busing.data.type.Time

data class BusRouteInfoModel(
    val id: Id,
    val name: String,
    val startStationId: Id,
    val startStationName: String,
    val endStationId: Id,
    val endStationName: String,
    val startFirstTime: Time,
    val startLastTime: Time,
    val finishFirstTime: Time,
    val finishLastTime: Time,
    val maxPeekAlloc: Time
)
