package com.san.busing.domain.model

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.PlateType

data class BusModel(
    val id: Id,
    val type: PlateType,
    val plateNumber: String,
    val sequenceNumber: Int,
    val isLowPlate: Boolean,
    val remainSeat: Int
)
