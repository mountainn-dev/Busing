package com.san.busing.domain.modelimpl

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.SearchResultModel

data class BusRouteSearchResultModelImpl(
    override val id: Id,
    override val name: String,
    val type: RouteType,
    val region: String
) : SearchResultModel