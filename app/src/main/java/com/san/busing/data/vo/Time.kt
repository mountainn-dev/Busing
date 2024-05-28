package com.san.busing.data.vo

import androidx.core.text.isDigitsOnly
import kotlin.math.abs

data class Time(
    private val value: String
) {
    val hour: Hour
    val minute: Minute

    init {
        require(value.contains(COLON))
        hour = Hour(value.split(COLON)[0])
        minute = Minute(value.split(COLON)[1])
    }

    fun getDifferenceWith(other: Time) =
        Time("${hour.getDifferenceWith(other.hour)}$COLON${minute.getDifferenceWith(other.minute)}")

    companion object {
        private const val COLON = ":"
        private const val MIN_HOUR = 0
        private const val MAX_HOUR = 24
        private const val MIN_MINUTE = 0
        private const val MAX_MINUTE = 59

        fun default(): Time = Time("00:00")
        fun hour(hour: Int): Time = Time("$hour${COLON}00")
        fun minute(minute: Int): Time = Time("00$COLON$minute")
    }

    inner class Hour(
        private val value: String
    ) {
        init {
            require(value.isDigitsOnly())
            require(value.toInt() in MIN_HOUR..MAX_HOUR)
        }

        fun get() = value.toInt()

        fun getDifferenceWith(other: Hour) = abs(get() - other.get())
    }

    inner class Minute(
        private val value: String
    ) {
        init {
            require(value.isDigitsOnly())
            require(value.toInt() in MIN_MINUTE..MAX_MINUTE)
        }

        fun get() = value.toInt()

        fun getDifferenceWith(other: Minute) = abs(get() - other.get())
    }
}
