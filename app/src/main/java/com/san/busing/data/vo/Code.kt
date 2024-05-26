package com.san.busing.data.vo

abstract class Code(
    private val value: Int
) {
    init {
        require(value > 0)
    }
}