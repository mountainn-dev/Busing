package com.san.busing.domain.model

import com.san.busing.data.type.CodeNumber
import com.san.busing.data.type.Id

data class BusRouteModel(
    val id: Id,
    val name: String,
    val typeCd: CodeNumber,
    val typeName: String,
    val region: String
)
