package com.san.busing.data

abstract class Result<T> {
    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> error(error: Exception): Result<T> = Error(error)
    }
}

class Success <T> (private val data: T) : Result<T>() {
    fun data() = data
}

class Error <T> (private val error: Exception) : Result<T>() {
    fun message(): String {
        return if (error.message.isNullOrBlank()) {
            "Unknown Exception"
        } else {
            error.message!!
        }
    }
}