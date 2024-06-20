package com.san.busing.data.exception

import java.io.IOException

/**
 * ServiceException
 *
 * Interceptor 에서 throw 하기 위한 ServiceResult 유형별 Exception 클래스
 */
object ServiceException {
    /**
     * SystemException
     *
     * Server, Interceptor(XmlPullParser, IO) 관련 에러를 전부 포함하는 Exception
     */
    class SystemException(override val message: String?) : IOException(message)

    /**
     * KeyException
     *
     * 서비스 키 인증과 관련된 Exception
     */
    class KeyException(override val message: String?) : IOException(message)

    /**
     * RequestException
     *
     * 서비스 요청과 관련된 Exception
     */
    class RequestException(override val message: String?) : IOException(message)

    /**
     * KeywordFormatException
     *
     * 검색 시 키워드 형식과 관련된 Exception
     */
    class KeywordFormatException(override val message: String?) : IOException(message)

    /**
     * IdException
     *
     * 데이터 ID 와 관련된 Exception
     */
    class IdException(override val message: String?) : IOException(message)

    /**
     * ParameterException
     *
     * 필수 요청 파라미터와 관련된 Exception
     */
    class ParameterException(override val message: String?) : IOException(message)

    /**
     * ResultException
     *
     * 검색 결과와 관련된 Exception
     */
    class ResultException(override val message: String?) : IOException(message)
}