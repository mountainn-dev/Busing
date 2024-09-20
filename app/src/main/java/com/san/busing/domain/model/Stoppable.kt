package com.san.busing.domain.model

/**
 * Stoppable
 *
 * 정류장에 정차할 수 있는 운송 수단 타입 인터페이스
 * 정류장 관련 데이터가 주입될 때, 해당 정류장 식별 데이터를 담는다.
 */
interface Stoppable{
    val stationSequence: Int

    fun setStationSequence(seqNum: Int)
}