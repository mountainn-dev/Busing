package com.san.busing.data.source.remote.interceptor

import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.source.remote.parser.XmlParser
import com.san.busing.data.source.remote.retrofit.ServiceResult.*
import okhttp3.Interceptor
import okhttp3.Response
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
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
     * 서비스 API 통신 결과에 따른 분기
     * 성공한 경우를 제외하고 ServiceResult 에 따라 Exception throw
     */
    private fun parseApiResult(inputStream: InputStream) {
        val resultCode: Int

        try {
            resultCode = XmlParser().parse(inputStream).toInt()
        } catch (e: XmlPullParserException) {
            throw ServiceException.SystemException(e.message)
        } catch (e: IOException) {
            throw ServiceException.SystemException(e.message)
        }

        when (resultCode) {
            SUCCESS.code -> {}
            SYSTEM_ERROR.code -> throw ServiceException.SystemException(SYSTEM_ERROR.message)
            SERVICE_NOT_READY.code -> throw ServiceException.SystemException(SERVICE_NOT_READY.message)
            NO_ESSENTIAL_PARAMETER.code -> throw ServiceException.ParameterException(NO_ESSENTIAL_PARAMETER.message)
            WRONG_ESSENTIAL_PARAMETER.code -> throw ServiceException.ParameterException(WRONG_ESSENTIAL_PARAMETER.message)
            NO_RESULT.code -> throw ServiceException.ResultException(NO_RESULT.message)
            NO_RESULT_BUS_ARRIVAL.code -> throw ServiceException.ResultException(NO_RESULT_BUS_ARRIVAL.message)
            NO_SERVICE_KEY.code -> throw ServiceException.KeyException(NO_SERVICE_KEY.message)
            WRONG_SERVICE_KEY.code -> throw ServiceException.KeyException(WRONG_SERVICE_KEY.message)
            UNAUTHORIZED_SERVICE_KEY.code -> throw ServiceException.KeyException(UNAUTHORIZED_SERVICE_KEY.message)
            OVER_REQUEST_LIMIT.code -> throw ServiceException.RequestException(OVER_REQUEST_LIMIT.message)
            WRONG_POSITION_REQUEST.code -> throw ServiceException.RequestException(WRONG_POSITION_REQUEST.message)
            WRONG_FORMAT_ROUTE_NAME.code -> throw ServiceException.KeywordFormatException(WRONG_FORMAT_ROUTE_NAME.message)
            WRONG_FORMAT_STATION_NAME.code -> throw ServiceException.KeywordFormatException(WRONG_FORMAT_STATION_NAME.message)
            WRONG_START_STATION_ID.code -> throw ServiceException.IdException(WRONG_START_STATION_ID.message)
            WRONG_ARRIVAL_STATION_ID.code -> throw ServiceException.IdException(WRONG_ARRIVAL_STATION_ID.message)
            else -> throw IOException(ExceptionMessage.NO_SERVICE_RESULT_EXCEPTION)
        }
    }
}