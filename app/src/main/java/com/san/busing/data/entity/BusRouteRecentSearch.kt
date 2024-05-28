package com.san.busing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.san.busing.data.vo.Id
import com.san.busing.domain.modelimpl.BusRouteRecentSearchModelImpl

@Entity
data class BusRouteRecentSearch(
    @PrimaryKey val id: Int,
    val name: String,
    val index: Long
) {
    fun toBusRouteRecentSearchModel() = BusRouteRecentSearchModelImpl(
        Id(id),
        name,
        index
    )
}
