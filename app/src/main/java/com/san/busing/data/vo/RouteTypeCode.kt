package com.san.busing.data.vo

import com.san.busing.domain.enums.RouteType

data class RouteTypeCode(private val value: Int) : Code(value) {
    init {
        require(RouteType.entries.count { it.code == value } != 0)
    }

    // TODO: 광역, 일반 등 버스 구분용 메서드 작성
}
