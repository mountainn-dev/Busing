package com.san.busing.data.vo

import com.san.busing.data.exception.ExceptionMessage
import java.io.Serializable

data class Id(
    private val value: Int
) : Serializable {
    init {
        require(value > 0) { ExceptionMessage.WRONG_ID_FORMAT_EXCEPTION }
    }

    fun get() = value
}