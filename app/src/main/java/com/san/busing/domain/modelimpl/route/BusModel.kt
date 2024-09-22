package com.san.busing.domain.modelimpl.route

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.PlateType

/**
 * BusModel
 *
 * 노선별 실시간 운행중인 버스 정보를 담는 클래스
 * 노선 상세 화면의 버스 위치 컨텐츠를 구성한다.
 */
data class BusModel(
    val id: Id,
    val type: PlateType,
    val plateNumber: String,
    val sequenceNumber: Int,
    val isLowPlate: Boolean,
    val remainSeat: Int
)

data class BusModels(
    private val data: List<BusModel>
) {
    private val models = data.sortedBy { it.sequenceNumber }

    fun get(index: Int) = models[index]
    fun getOrNullBySeq(sequenceNumber: Int): BusModel? {
        for (bus in models) {
            if (bus.sequenceNumber == sequenceNumber) return bus
        }

        return null
    }
    fun count() = models.size
    fun indices() = models.indices

    companion object {
        fun instance() = BusModels(listOf())
    }
}
