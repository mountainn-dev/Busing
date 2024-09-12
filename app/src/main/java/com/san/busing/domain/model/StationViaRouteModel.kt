package com.san.busing.domain.model

data class StationViaRouteModel(
    val routeSummary: RouteSummaryModel,
    val sequenceNumber: Int
)

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
