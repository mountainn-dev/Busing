package com.san.busing.domain.model.route

import com.san.busing.data.vo.Id
import com.san.busing.domain.enums.RouteType
import java.io.Serializable

interface RouteModel : Serializable {
    val id: Id
    val type: RouteType
    val name: String
    val regionName: String

    fun isSame(id: Id): Boolean
}