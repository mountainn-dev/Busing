package com.san.busing.domain.model

import com.san.busing.data.vo.Code
import com.san.busing.data.vo.Id

data class BusRouteModel(
    val id: Id,
    val name: String,
    val typeCd: Code,
    val typeName: String,
    val region: String
)
