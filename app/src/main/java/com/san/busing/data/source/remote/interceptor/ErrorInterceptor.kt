package com.san.busing.data.source.remote.interceptor

import com.san.busing.data.source.remote.parser.XmlParser
import com.san.busing.data.source.remote.retrofit.ServiceResult.*
import com.san.busing.data.exception.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.InputStream

/**
 * ErrorInterceptor
 *
 * 서비스 API 로부터 응답을 받았을 때, 특정 상황의 경우 응답을 그대로 반환하지 않고 Exception 을 throw 하기 위한 클래스
 */
class ErrorInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        when (response.code()) {
            200 -> parseApiResult(response.body()!!.byteStream())
            else -> throw RuntimeException()
        }

        response.close()
        return chain.proceed(chain.request())
    }

    /**
     * fun parseApiResult(): void
     *
     * API 결과 코드값에 따른 분기
     */
    private fun parseApiResult(inputStream: InputStream) {
        val resultCode = XmlParser().parse(inputStream)

        when (resultCode.toInt()) {
            SUCCESS.code -> {}
            NO_RESULT.code -> throw ApiException.NoResultException(NO_RESULT.message)
            else -> throw RuntimeException()
        }
    }
}