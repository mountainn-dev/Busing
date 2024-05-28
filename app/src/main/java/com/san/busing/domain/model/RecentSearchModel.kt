package com.san.busing.domain.model

import com.san.busing.data.vo.Id

interface RecentSearchModel {
    val id: Id
    val name: String
    val index: Long
}