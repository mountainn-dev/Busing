package com.san.busing.domain.enums

import com.san.busing.domain.utils.Const

/**
 * PlateType
 *
 * 경기도 공공 데이터 포털에서 제공하는 버스 차량 유형
 */
enum class PlateType(val code: Int, val typeName: String, val tag: String) {
    NONE(0, "정보없음", Const.TAG_NONE),
    COMPACT(1, "소형승합차", Const.TAG_COMPACT),
    MID_SIZE(2, "중형승합차", Const.TAG_MID_SIZE),
    FULL_SIZE(3, "대형승합차", Const.TAG_FULL_SIZE),
    DOUBLE_DECKER(4, "2층버스", Const.TAG_DOUBLE_DECKER)
}