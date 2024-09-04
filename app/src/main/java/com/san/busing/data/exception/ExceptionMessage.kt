package com.san.busing.data.exception

object ExceptionMessage {
    // Exception Logging Tag
    const val TAG_ERROR_INTERCEPTOR_EXCEPTION = "Error Interceptor Exception"
    const val TAG_ROUTE_INFO_EXCEPTION = "Route Info Exception"
    const val TAG_ROUTE_SUMMARY_EXCEPTION = "Route Summary Exception"
    const val TAG_ROUTE_STATION_EXCEPTION = "Route Station Exception"
    const val TAG_ROUTE_RECENT_SEARCH_EXCEPTION = "Route Recent Search Exception"
    const val TAG_BUS_EXCEPTION = "Bus Exception"
    const val TAG_BUS_ARRIVAL_EXCEPTION = "Bus Arrival Exception"
    const val TAG_STATION_SUMMARY_EXCEPTION = "Station Summary Exception"

    // Service Exception Message
    const val NO_SERVICE_RESULT_EXCEPTION = "서비스 결과 코드가 존재하지 않습니다."
    const val NO_RESULT_EXCEPTION = "결과 데이터가 존재하지 않습니다."

    // Service Exception Message For UI Layer
    const val SERVICE_FAIL_EXCEPTION = "데이터 요청에 실패하였습니다."
    const val INTERNET_CONNECTION_FAIL_EXCEPTION = "인터넷 연결에 실패하였습니다."
    const val NO_ESSENTIAL_PARAMETER_EXCEPTION = "입력값을 다시 확인해주시기 바랍니다."

    // Entity Parsing Exception Message
    const val WRONG_ID_FORMAT_EXCEPTION = "id 형식이 올바르지 않습니다."
    const val NO_ROUTE_TYPE_EXCEPTION = "노선 유형이 존재하지 않습니다."
    const val NO_PLATE_TYPE_EXCEPTION = "차량 유형이 존재하지 않습니다."
    const val WRONG_PLATE_NUMBER_FORMAT_EXCEPTION = "차량 번호 형식이 올바르지 않습니다."
    const val WRONG_LOW_PLATE_VALUE_EXCEPTION = "저상 버스 여부 값이 올바르지 않습니다."
    const val WRONG_REMAIN_SEAT_VALUE_EXCEPTION = "차량 빈자리 수 값이 올바르지 않습니다."
    const val WRONG_TURNAROUND_VALUE_EXCEPTION = "회차점 여부 값이 올바르지 않습니다."
    const val WRONG_TIME_FORMAT_EXCEPTION = "노선 시간 형식이 올바르지 않습니다."
}