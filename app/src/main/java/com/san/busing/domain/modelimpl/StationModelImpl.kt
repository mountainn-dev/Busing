package com.san.busing.domain.modelimpl

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.Passable
import com.san.busing.domain.model.StationModel
import com.san.busing.domain.utils.Const

data class StationModelImpl(
    override val id: Id,
    override val mobileNo: String,
    override val name: String,
    override val regionName: String
) : StationModel, Passable {
    override val vehicleId: Id
        get() = passVehicleId
    private var passVehicleId = Id.instance()
    override val stationSequence: Int
        get() = sequenceNumber
    private var sequenceNumber = Const.ZERO
    override val isTurnaround: Boolean
        get() = turnYesOrNo
    private var turnYesOrNo = false

    override fun isSame(id: Id) = this.id == id
    override fun setVehicleId(id: Id) {
        passVehicleId = id
    }
    override fun setStationSequence(seqNum: Int) {
        sequenceNumber = seqNum
    }
    override fun setIsTurnaround(turnYn: Boolean) {
        turnYesOrNo = turnYn
    }
}

data class StationModels(
    private val data: List<StationModel>
) {
    private var models = data

    fun sort() {
        models = data.sortedWith(
            compareBy<StationModel>{ it.name.length }.thenBy { it.name }.thenBy { it.regionName }
        )
    }
    fun get(index: Int) = models[index]
    fun count() = models.size
    fun isEmpty() = models.isEmpty()
    fun turnaroundSequence(): Int? {
        for (sta in data) {
            val station = sta as Passable

            if (station.isTurnaround) return station.stationSequence
        }

        return null
    }
    fun getOrFirst(seqNum: Int) = if (seqNum !in data.indices) data.get(0) else data.get(seqNum)

    companion object {
        fun instance() = StationModels(listOf())
    }
}
