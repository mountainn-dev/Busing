package com.san.busing.domain.modelimpl.station

import com.san.busing.data.vo.Id
import com.san.busing.domain.model.Passable
import com.san.busing.domain.model.station.StationModel
import com.san.busing.domain.utils.Const
import kotlin.math.absoluteValue

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

// TODO: 정류장 검색, 노선 경유 정류장이 StationModels 를 공유하는데 sorting 관련해서 리팩토링 필요
// 우선은 sort() 를 별도로 만들어서 repo 에서 정류장 검색 결과를 받아오는 경우에만 sorting 을 실행
data class StationModels(
    private val data: List<StationModel>
) {
    private var models = data

    fun sort(keyword: String) {
        models = data.sortedWith(
            compareBy<StationModel> { it.name.compareTo(keyword).absoluteValue }.thenBy { it.name }
                .thenBy { it.regionName }
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
    fun getOrFirst(seqNum: Int) = if (seqNum !in data.indices) data[0] else data[seqNum]

    companion object {
        fun instance() = StationModels(listOf())
    }
}
