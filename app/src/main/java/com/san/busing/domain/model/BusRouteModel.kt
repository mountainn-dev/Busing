package com.san.busing.domain.model

import com.san.busing.data.entity.CodeNumber
import com.san.busing.data.entity.Id

data class BusRouteModel(
    val id: Id,
    val name: String,
    val typeCd: CodeNumber,
    val typeName: String,
    val region: String
)
