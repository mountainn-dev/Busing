package com.san.busing.data.type

data class Id(
    private val value: Int
) {
    init {
        require(value > 0)
    }

    fun get() = value
}