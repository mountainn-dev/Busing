package com.san.busing.domain.model

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType

data class RouteSummaryModel(
    val id: Id,
    val type: RouteType,
    val name: String,
    val region: String
)