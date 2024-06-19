package com.san.busing.domain.model

import com.san.busing.data.vo.Id

data class RouteInfoModel(
    val id: Id,
    val name: String,
    val startStationId: Id,
    val startStationName: String,
    val endStationId: Id,
    val endStationName: String,
//    val startFirstTime: Time,
//    val startLastTime: Time,
//    val finishFirstTime: Time,
//    val finishLastTime: Time,
//    val maxPeekAlloc: Time
)
