package com.san.busing.data

import kotlin.Exception

abstract class Result<T> {
    companion object {
        /**
         * Result Success 팩토리 함수
         *
         * 데이터가 multiple 타입일 때 empty 상태인 경우 Error 타입으로 반환
         * 레포지토리에서 결과에 따른 Result 객체를 반환할 때, 값이 성공적으로 반환되었더라도
         * 비어있는 값을 반환한 경우에는 Error 타입으로 전환되도록 작성
         */
        fun <T> success(data: T): Result<T> {
            if (data is List<*> && data.isEmpty())
                return Error(Exception(ExceptionMessage.NO_DATA_COLLECTION_EXCEPTION))

            return Success(data)
        }
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