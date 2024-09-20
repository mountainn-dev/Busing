package com.san.busing.domain.model

/**
 * Stoppable
 *
 * 정류장에 정차하는 노선 관련 인터페이스
 * 노선 데이터가 정류장과 연관될 때, 해당 노선만의 정류장 식별 데이터를 담는다.
 */
interface Stoppable : RouteModel{
    val stationSequence: Int

    fun setStationSequence(seqNum: Int)
}