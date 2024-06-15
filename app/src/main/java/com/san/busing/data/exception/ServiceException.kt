package com.san.busing.data.exception

import java.io.IOException

/**
 * ServiceException
 *
 * Interceptor 에서 throw 하기 위한 ServiceResult 유형별 Exception 클래스
 */
object ServiceException {
    class SystemException(override val message: String?) : IOException(message)
    class KeyException(override val message: String?) : IOException(message)
    class RequestException(override val message: String?) : IOException(message)
    class KeywordFormatException(override val message: String?) : IOException(message)
    class IdException(override val message: String?) : IOException(message)
    class ParameterException(override val message: String?) : IOException(message)
    class ResultException(override val message: String?) : IOException(message)
}