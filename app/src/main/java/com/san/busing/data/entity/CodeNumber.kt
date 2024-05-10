package com.san.busing.data.entity

import androidx.core.text.isDigitsOnly

data class CodeNumber(
    private val value: Int
) {
    init {
        require(value > 0)
    }

    fun get() = value
}
