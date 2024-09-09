package com.san.busing.domain.model

data class StationViaRouteModel(
    val routeSummary: RouteSummaryModel,
    val sequenceNumber: Int
)

data class StationViaRouteModels(
    private var models: List<StationViaRouteModel>
) {
    init {
        models = models.sortedBy { it.routeSummary.name }
    }

    fun get(index: Int) = models[index]
    fun count() = models.size
}
