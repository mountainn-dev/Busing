package com.san.busing.data.exception

import java.io.IOException

/**
 * ServiceException
 *
 * Interceptor 에서 throw 하기 위한 ServiceResult 유형별 Exception 클래스
 */
object ServiceException {
    /**
     * SystemException (Critical)
     *
     * System, Interceptor(XmlPullParser, IO) 파싱 관련 에러 등 API 서버 에러로 정의되지는 않았지만
     * 접근 권한 혹은 서버 응답 관련 문제일 때 발생하는 Exception
     */
    class SystemException(override val message: String?) : IOException(message)

    /**
     * ResultException (Critical by Situation)
     *
     * 데이터 조회 시 일치하는 결과 값이 존재하지 않을 때 발생하는 Exception
     */
    class ResultException(override val message: String?) : IOException(message)

    /**
     * EssentialParameterException
     *
     * 필수 파라미터가 생략되거나 포맷이 올바르지 않을 때 발생하는 Exception
     */
    class EssentialParameterException(override val message: String?) : IOException(message)

    /**
     * OptionalParameterException
     *
     * 파라미터가 생략되거나 포맷이 올바르지 않을 때 발생하는 Exception
     */
    class OptionalParameterException(override val message: String?) : IOException(message)
}