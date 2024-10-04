package com.san.busing.domain.modelimpl.route

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.Stoppable
import com.san.busing.domain.model.route.RouteModel
import com.san.busing.domain.utils.Const
import kotlin.math.absoluteValue

data class RouteModelImpl(
    override val id: Id,
    override val type: RouteType,
    override val name: String,
    override val regionName: String,
) : RouteModel, Stoppable {
    override val stationSequence: Int
        get() = sequenceNumber
    private var sequenceNumber = Const.ZERO

    override fun isSame(id: Id) = this.id == id

    override fun setStationSequence(seqNum: Int) {
        sequenceNumber = seqNum
    }
}

// TODO: 노선 검색, 정류장 경유 노선이 RouteModels 를 공유하는데 sorting 관련해서 리팩토링 필요
// 우선은 sort() 를 별도로 만들어서 repo 에서 노선 검색 결과를 받아오는 경우에만 sorting 을 실행
data class RouteModels(
    private val data: List<RouteModel>,
) {
    private var models = data

    fun sort() {
        models = data.sortedWith(compareBy<RouteModel> { it.name }.thenBy { it.regionName })
    }

    fun sort(keyword: String) {
        models =
            data.sortedWith(
                compareBy<RouteModel> { it.name.compareTo(keyword).absoluteValue }.thenBy { it.name }
                    .thenBy { it.regionName },
            )
    }

    fun get() = models

    fun get(index: Int) = models[index]

    fun count() = models.size

    fun isEmpty() = models.isEmpty()

    companion object {
        fun instance() = RouteModels(listOf())
    }
}
