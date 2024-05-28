package com.san.busing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.san.busing.data.vo.Id
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.utils.Const

@Entity
data class BusRouteRecentSearch(
    @PrimaryKey val id: Int,
    val name: String,
    val index: Long
) {
    fun toBusRouteRecentSearchModel() = BusRouteRecentSearchModel(
        Id(id),
        name,
        index
    )
}
