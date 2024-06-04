package com.san.busing.domain.enums

import com.san.busing.domain.utils.Const

/**
 * RouteType
 *
 * 경기도 공공 데이터 포털에서 제공하는 노선 유형
 */
enum class RouteType(val code: Int, val typeName: String, val tag: String) {
    AREA_DIRECT(11, "직행좌석형시내버스", Const.TAG_AREA),
    NORMAL_SEAT(12, "좌석형시내버스", Const.TAG_NORMAL),
    NORMAL(13, "일반형시내버스", Const.TAG_NORMAL),
    AREA_EXPRESS(14, "광역급행형시내버스", Const.TAG_AREA),
    CIRCULAR(16, "경기순환버스", Const.TAG_CIRCULAR),
    RURAL_DIRECT(21, "직행좌석형농어촌버스", Const.TAG_RURAL),
    RURAL_SEAT(22, "좌석형농어촌버스", Const.TAG_RURAL),
    RURAL_NORMAL(23, "일반형농어촌버스", Const.TAG_RURAL),
    VILLAGE(30, "마을버스", Const.TAG_VILLAGE),
    OUT_TOWN_EXPRESS(41, "고속형시외버스", Const.TAG_OUT_TOWN),
    OUT_TOWN_SEAT(42, "좌석형시외버스", Const.TAG_OUT_TOWN),
    OUT_TOWN_NORMAL(43, "일반형시외버스", Const.TAG_OUT_TOWN),
    AIRPORT_LIMO(51, "리무진형공항버스", Const.TAG_AIRPORT),
    AIRPORT_SEAT(52, "좌석형공항버스", Const.TAG_AIRPORT),
    AIRPORT_NORMAL(53, "일반형공항버스", Const.TAG_AIRPORT),
}