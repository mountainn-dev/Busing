package com.san.busing.domain.modelimpl

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.RouteModel
import com.san.busing.domain.model.Stoppable
import com.san.busing.domain.utils.Const

data class RouteModelImpl(
    override val id: Id,
    override val type: RouteType,
    override val name: String,
    override val regionName: String,
) : Stoppable {
    override val stationSequence: Int
        get() = sequenceNumber
    private var sequenceNumber = Const.ZERO

    override fun isSame(id: Id) = this.id == id
    override fun setStationSequence(seqNum: Int) {
        sequenceNumber = seqNum
    }
}

data class RouteModels(private val data: List<Stoppable>) {
    private val models = data.sortedWith(
        compareBy<Stoppable>{ it.name.length }.thenBy { it.name }.thenBy { it.regionName }
    )

    fun get() = models
    fun get(index: Int) = models[index]
    fun count() = models.size
    fun isEmpty() = models.isEmpty()

    companion object {
        fun instance() = RouteModels(listOf())
    }
}