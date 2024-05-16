package com.san.busing.data.type

data class CodeNumber(
    private val value: Int
) {
    init {
        require(value > 0)
    }

    fun get() = value
}