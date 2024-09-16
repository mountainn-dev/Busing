package com.san.busing.domain.model

import com.san.busing.data.vo.Id

data class StationViaRouteModel(
    val routeSummary: RouteSummaryModel,
    val sequenceNumber: Int
) {
    fun isSame(id: Id) = routeSummary.id == id
}

data class StationViaRouteModels(
    private val data: List<StationViaRouteModel>
) {
    private val models = data.sortedBy { it.routeSummary.name }

    fun get() = models
    fun get(index: Int) = models[index]
    fun count() = models.size

    companion object {
        fun instance() = StationViaRouteModels(listOf())
    }
}
