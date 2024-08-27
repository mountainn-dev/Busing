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
)

data class RouteStationModels(
    private var models: List<RouteStationModel>
) {
    init {
        models = models.sortedBy { it.sequenceNumber }
    }

    fun get(index: Int) = models[index]
    fun count() = models.size
    fun turnaroundSeqNum() = models.find { it.isTurnaround }?.sequenceNumber
}
