package com.san.busing.data.exception

object ExceptionMessage {
    const val TAG_ROUTE_INFO_EXCEPTION = "Route Info Exception"
    const val TAG_ROUTE_SUMMARY_EXCEPTION = "Route Summary Exception"
    const val TAG_ROUTE_STATION_EXCEPTION = "Route Station Exception"
    const val TAG_ROUTE_RECENT_SEARCH_EXCEPTION = "Route Recent Search Exception"
    const val TAG_BUS_EXCEPTION = "Bus Exception"

    const val UNSTABLE_SERVICE_EXCEPTION = "현재 서버 상태가 불안정합니다."
    const val UNSTABLE_INTERNET_CONNECTION = "인터넷 연결 상태가 불안정합니다."
    const val NO_SERVICE_RESULT_EXCEPTION = "서비스 결과 코드가 존재하지 않습니다."
    const val WRONG_ID_FORMAT_EXCEPTION = "id 형식이 올바르지 않습니다."
    const val NO_ROUTE_TYPE_EXCEPTION = "노선 유형이 존재하지 않습니다."
    const val NO_PLATE_TYPE_EXCEPTION = "차량 유형이 존재하지 않습니다."
    const val WRONG_PLATE_NUMBER_FORMAT_EXCEPTION = "차량 번호 형식이 올바르지 않습니다."
    const val WRONG_LOW_PLATE_VALUE_EXCEPTION = "저상 버스 여부 값이 올바르지 않습니다."
    const val WRONG_REMAIN_SEAT_VALUE_EXCEPTION = "차량 빈자리 수 값이 올바르지 않습니다."
    const val WRONG_TURNAROUND_VALUE_EXCEPTION = "회차점 여부 값이 올바르지 않습니다."
}