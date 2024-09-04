package com.san.busing.data.entity

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.model.StationSummaryModel
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * StationSummary
 *
 * 정류장 검색 시 정류장 요약 정보를 담는 클래스
 * 정류장 검색 화면 검색 결과를 구성한다.
 */
@Xml(name = "busStationList")
data class StationSummary(
    @PropertyElement val stationId: Int,
    @PropertyElement val stationName: String,
    @PropertyElement val mobileNo: String?,
    @PropertyElement val regionName: String,
    @PropertyElement val districtCd: Int,
    @PropertyElement val centerYn: String,
    @PropertyElement(name = "x") val longitude: Double,
    @PropertyElement(name = "y") val latitude: Double
) {
    fun toStationSummaryModel() = StationSummaryModel(
        Id(stationId),
        mobileNo(mobileNo),
        stationName,
        regionName
    )

    private fun mobileNo(mobileNo: String?) = when(mobileNo.isNullOrBlank()) {
        true -> NO_MOBILE_NUMBER
        false -> mobileNo
    }

    companion object {
        private const val NO_MOBILE_NUMBER = "00000"
    }
}

@Xml
data class StationSummaries(
    @Path("msgBody") @Element val stationSummaries: List<StationSummary>
) {
    fun get(): List<StationSummaryModel> {
        return stationSummaries.map { it.toStationSummaryModel() }.toList()
    }
}