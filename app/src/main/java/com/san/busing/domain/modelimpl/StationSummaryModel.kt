package com.san.busing.domain.modelimpl

import com.san.busing.data.vo.Id

/**
 * StationSummaryModel
 *
 * 정류장 검색 시 정류장 요약 정보를 담는 클래스
 * 정류장 검색 화면 검색 결과를 구성한다.
 */
data class StationSummaryModel(
    val id: Id,
    val mobileNo: String,
    val name: String,
    val region: String
)

data class StationSummaryModels(
    private val data: List<StationSummaryModel>
) {
    private val models = data.sortedWith(
        compareBy<StationSummaryModel>{ it.name.length }.thenBy { it.name }.thenBy { it.region }
    )

    fun get(index: Int) = models[index]
    fun count() = models.size
    fun isEmpty() = models.isEmpty()

    companion object {
        fun instance() = StationSummaryModels(listOf())
    }
}
