package com.san.busing.data.source.remote.retrofit

enum class ServiceResult(val code: Int, val message: String) {
    SUCCESS(0, "정상적으로 처리되었습니다."),
    SYSTEM_ERROR(1, "시스템 에러가 발생하였습니다."),
    NO_ESSENTIAL_PARAMETER(2, "필수 요청 Parameter 가 존재하지 않습니다."),
    WRONG_ESSENTIAL_PARAMETER(3, "필수 요청 Parameter가 잘못되었습니다"),
    NO_RESULT(4, "결과가 존재하지 않습니다."),
    NO_SERVICE_KEY(5, "필수 요청 Parameter (인증키) 가 존재하지 않습니다."),
    WRONG_SERVICE_KEY(6, "등록되지 않은 키입니다."),
    UNAUTHORIZED_SERVICE_KEY(7, "사용할 수 없는(등록은 되었으나, 일시적으로 사용 중지된) 키입니다."),
    OVER_REQUEST_LIMIT(8, "요청 제한을 초과하였습니다."),
    WRONG_POSITION_REQUEST(20, "잘못된 위치로 요청하였습니다. 위경도 좌표값이 정확한지 확인하십시오."),
    WRONG_FORMAT_ROUTE_NAME(21, "노선번호는 1 자리 이상 입력하세요."),
    WRONG_FORMAT_STATION_NAME(22, "정류소명/번호는 1자리 이상 입력하세요."),
    NO_RESULT_BUS_ARRIVAL(23, "버스 도착 정보가 존재하지 않습니다."),
    WRONG_START_STATION_ID(31, "존재하지 않는 출발 정류소 아이디(ID)/번호입니다."),
    WRONG_ARRIVAL_STATION_ID(32, "존재하지 않는 도착 정류소 아이디(ID)/번호입니다."),
    SERVICE_NOT_READY(99, "API 서비스 준비중입니다.")
}