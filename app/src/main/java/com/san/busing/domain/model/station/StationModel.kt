package com.san.busing.domain.model.station

import com.san.busing.data.vo.Id
import java.io.Serializable

interface StationModel : Serializable {
    val id: Id
    val mobileNo: String
    val name: String
    val regionName: String

    fun isSame(id: Id) = this.id == id
}