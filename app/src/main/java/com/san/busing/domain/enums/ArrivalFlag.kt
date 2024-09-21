package com.san.busing.domain.enums

/**
 * ArrivalFlag
 *
 * 경기도 버스 정보에서 제공하는 버스 도착 상태 유형
 */
enum class ArrivalFlag(val flagName: String) {
    RUN("운행중"),
    PASS("운행중"),
    STOP("운행 종료"),
    WAIT("회차지 대기");

    companion object {
        fun find(code: String) = valueOf(code)
    }
}