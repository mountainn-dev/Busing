package com.san.busing.data.vo

import java.io.Serializable

data class Id(
    private val value: Int
) : Serializable {
    init {
        require(value > 0)
    }

    fun get() = value
}