package com.san.busing.data

import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.Exception

abstract class Result<T> {
    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> error(error: Exception): Result<T> = Error(error)
    }
}

class Success <T> (val data: T) : Result<T>()

class Error <T> (private val error: Exception) : Result<T>() {
    fun message() =
        if (error is UnknownHostException) ExceptionMessage.INTERNET_CONNECTION_FAIL_EXCEPTION
        else error.message ?: error.toString()

    fun isCritical() =
        error is UnknownHostException || error is ServiceException.SystemException
    fun isTimeOut() = error is SocketTimeoutException
}