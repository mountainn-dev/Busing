package com.san.busing.domain.model

import com.san.busing.data.vo.Id

/**
 * Passable
 *
 * 운송 수단을 수용할 수 있는 정류장 타입 인터페이스
 * 운송 수단 관련 데이터가 주입될 때, 해당 운송 수단 식별 데이터 및 운송 수단 기준 정류장 식별 데이터를 담는다.
 */
interface Passable {
    val vehicleId: Id
    val stationSequence: Int
    val isTurnaround: Boolean

    fun setVehicleId(id: Id)
    fun setStationSequence(seqNum: Int)
    fun setIsTurnaround(turnYn: Boolean)
}