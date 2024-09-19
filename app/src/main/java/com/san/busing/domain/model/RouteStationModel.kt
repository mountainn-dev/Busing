package com.san.busing.domain.model

import com.san.busing.data.vo.Id

/**
 * RouteStationModel
 *
 * 노선 상세 화면의 정류장 아이템 정보를 담는 클래스
 * 버스 노선 상세 화면 컨텐츠를 구성한다.
 */
data class RouteStationModel(
    val id: Id,
    val number: String,
    val name: String,
    val sequenceNumber: Int,
    val isTurnaround: Boolean,
    val positionX: Double,
    val positionY: Double
) {
    private var routeId = Id.instance()

    fun setViaRouteId(id: Id) {
        routeId = id
    }

    fun getViaRouteId() = routeId
}

data class RouteStationModels(
    private val data: List<RouteStationModel>
) {
    private val models = data.sortedBy { it.sequenceNumber }

    fun get(index: Int) = models[index]
    fun getOrFirst(index: Int) = if (index !in models.indices) models.first() else models[index]
    fun count() = models.size
    fun turnaroundSeqNum() = models.find { it.isTurnaround }?.sequenceNumber

    companion object {
        fun instance() = RouteStationModels(listOf())
    }
}
