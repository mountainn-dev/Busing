package com.san.busing.domain.utils

object Const {
    const val EMPTY_TEXT = ""
    const val ZERO = 0
    const val NO_DATA = -1

    // Intent Tag
    const val TAG_ROUTE_ID = "routeId"
    const val TAG_ROUTE_NAME = "routeName"
    const val TAG_ROUTE_TYPE = "routeType"

    // Plate Type Tag
    const val TAG_NONE = "-"
    const val TAG_COMPACT = "소형"
    const val TAG_MID_SIZE = "중형"
    const val TAG_FULL_SIZE = "대형"
    const val TAG_DOUBLE_DECKER = "2층"

    // Route Type Tag
    const val TAG_AIRPORT = "공항"
    const val TAG_AREA = "광역"
    const val TAG_CIRCULAR = "순환"
    const val TAG_NORMAL = "일반"
    const val TAG_OUT_TOWN = "시외"
    const val TAG_RURAL = "농어촌"
    const val TAG_VILLAGE = "마을"

    // message
    const val ROUTE_BUS_COUNT = "%d대"
    const val REMAIN_SEAT_COUNT = "%d석"
    const val NO_REMAIN_SEAT_COUNT = "-석"
    const val SERVICE_ERROR = "현재 서버 상태가 불안정합니다."
}