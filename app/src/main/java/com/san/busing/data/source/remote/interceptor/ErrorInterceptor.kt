package com.san.busing.data.source.remote.interceptor

import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.source.remote.parser.XmlParser
import com.san.busing.data.source.remote.retrofit.ServiceResult.NO_RESULT
import com.san.busing.data.source.remote.retrofit.ServiceResult.SUCCESS
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
        val request = chain.request()
        val response = chain.proceed(request)
        val body = response.body()

        parseApiResult(body!!.byteStream())
        return chain.proceed(request)
    }

    /**
     * fun parseApiResult(): void
     *
     * API 결과 코드값에 따른 분기
     */
    private fun parseApiResult(inputStream: InputStream) {
        var resultCode = -1

        try {
            resultCode = XmlParser().parse(inputStream).toInt()
        } catch (e: Exception) {
            throw Exception(e.message)
        }

        when (resultCode) {
            SUCCESS.code -> {}
            NO_RESULT.code -> throw ServiceException.NoResultException(NO_RESULT.message)
            else -> throw Exception(ExceptionMessage.NO_SERVICE_RESULT_CODE)
        }
    }
}