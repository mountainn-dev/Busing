package com.san.busing.data.vo

import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.utils.Const

data class RouteType(private val code: Int) {
    private lateinit var type: RouteType

    init {
        require(isRegisteredRouteType())
        initRouteType()
    }

    private fun isRegisteredRouteType() = RouteType.entries.any { it.code == code }
    private fun initRouteType() { type = RouteType.entries.find { it.code == code }!! }

    fun name() = type.tag
    fun isAirport() = type.tag == Const.TAG_AIRPORT
    fun isArea() = type.tag == Const.TAG_AREA
    fun isCircular() = type.tag == Const.TAG_CIRCULAR
    fun isNormal() = type.tag == Const.TAG_NORMAL
    fun isOutTown() = type.tag == Const.TAG_OUT_TOWN
    fun isRural() = type.tag == Const.TAG_RURAL
    fun isVillage() = type.tag == Const.TAG_VILLAGE
}
