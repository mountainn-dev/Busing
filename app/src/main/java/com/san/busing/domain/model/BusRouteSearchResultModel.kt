package com.san.busing.domain.model

import com.san.busing.data.vo.Id
import com.san.busing.data.vo.RouteType

data class BusRouteSearchResultModel(
    val id: Id,
    val name: String,
    val type: RouteType,
    val region: String
)
