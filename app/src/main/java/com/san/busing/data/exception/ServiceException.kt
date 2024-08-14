package com.san.busing.data.exception

import java.io.IOException

/**
 * ServiceException
 *
 * Interceptor 에서 throw 하기 위한 ServiceResult 유형별 Exception 클래스
 */
object ServiceException {
    /**
     * ServerException (Critical)
     *
     * Server, Interceptor(XmlPullParser, IO) 관련 에러 등 서비스 서버로부터 발생한 액션이 원인이 될 때 발생하는 Exception
     */
    class ServerException(override val message: String?) : IOException(message)

    /**
     * EssentialParameterException (Critical)
     *
     * 필수 입력값이 생략되거나 포맷이 올바르지 않을 때 발생하는 Exception
     */
    class EssentialParameterException(override val message: String?) : IOException(message)

    /**
     * OptionalParameterException
     *
     * 입력값이 생략되거나 포맷이 올바르지 않을 때 발생하는 Exception
     */
    class OptionalParameterException(override val message: String?) : IOException(message)

    /**
     * ResultException
     *
     * 데이터 조회 시 일치하는 결과 값이 존재하지 않을 때 발생하는 Exception
     */
    class ResultException(override val message: String?) : IOException(message)
}