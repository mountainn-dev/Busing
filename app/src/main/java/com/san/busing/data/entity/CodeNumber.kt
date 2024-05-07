package com.san.busing.data.entity

import androidx.core.text.isDigitsOnly

data class CodeNumber(
    private val value: String
) {
    init {
        require(value.isDigitsOnly())
        require(value.toInt() > 0)
    }

    fun get() = value.toInt()
}
