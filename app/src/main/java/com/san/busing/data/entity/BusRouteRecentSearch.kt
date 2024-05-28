package com.san.busing.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteRecentSearchModel

@Entity
data class BusRouteRecentSearch(
    @PrimaryKey val id: Int,
    val name: String
) {
    fun toBusRouteRecentSearchModel() = BusRouteRecentSearchModel(
        Id(id),
        name
    )
}
