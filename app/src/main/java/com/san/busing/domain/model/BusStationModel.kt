package com.san.busing.domain.model

import com.san.busing.data.vo.Id

data class BusStationModel(
    val id: Id,
    val name: String,
    val sequenceNumber: Int,
    val positionX: Double,
    val positionY: Double
)
