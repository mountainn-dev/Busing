package com.san.busing.data.vo

import com.san.busing.domain.enums.RouteType

data class RouteType(private val code: Int) {
    private lateinit var type: RouteType

    init {
        require(isRegisteredRouteType())
        initRouteType()
    }

    private fun isRegisteredRouteType() = RouteType.entries.any { it.code == code }
    private fun initRouteType() {
        type = RouteType.entries.find { it.code == code }!!
    }
    fun name() = type.tag
}
