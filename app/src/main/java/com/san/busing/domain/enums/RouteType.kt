package com.san.busing.domain.enums

enum class RouteType(val code: Int, val typeName: String) {
    TYPE_1(11, "직행좌석형시내버스"),
    TYPE_2(12, "좌석형시내버스"),
    TYPE_3(13, "일반형시내버스"),
    TYPE_4(14, "광역급행형시내버스"),
    TYPE_5(15, "따복형 시내버스"),
    TYPE_6(16, "경기순환버스"),
    TYPE_7(21, "직행좌석형농어촌버스"),
    TYPE_8(22, "좌석형농어촌버스"),
    TYPE_9(23, "일반형농어촌버스"),
    TYPE_10(30, "마을버스"),
    TYPE_11(41, "고속형시외버스"),
    TYPE_12(42, "좌석형시외버스"),
    TYPE_13(43, "일반형시외버스"),
    TYPE_14(51, "리무진공항버스"),
    TYPE_15(52, "좌석형공항버스"),
    TYPE_16(53, "일반형공항버스"),
}