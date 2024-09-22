package com.san.busing.data.source.remote.interceptor

import android.util.Log
import com.san.busing.data.exception.ExceptionMessage
import com.san.busing.data.exception.ServiceException
import com.san.busing.data.source.remote.parser.XmlParser
import com.san.busing.data.source.remote.retrofit.ServiceResult
import com.san.busing.data.source.remote.retrofit.ServiceResult.*
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
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

        val bodyCopy = body?.let {
            val source = it.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()
            ResponseBody.create(body.contentType(), body.contentLength(), buffer.clone())
        }

        parseResult(body!!.byteStream())
        return response.newBuilder().body(bodyCopy).build()
    }

    /**
     * fun parseApiResult(): void
     *
     * 서비스 API 통신 결과에 따른 분기
     * 성공한 경우를 제외하고 ServiceResult 에 따라 Exception throw
     */
    private fun parseResult(inputStream: InputStream) {
        val serviceResult: ServiceResult

        try {
            val resultCode = XmlParser().parse(inputStream).toInt()
            serviceResult = getServiceResultBy(resultCode)
        } catch (e: NoSuchElementException) {
            Log.e(ExceptionMessage.TAG_ERROR_INTERCEPTOR_EXCEPTION, e.toString())
            throw ServiceException.ServerException(ExceptionMessage.NO_SERVICE_RESULT_EXCEPTION)
        } catch (e: Exception) {
            Log.e(ExceptionMessage.TAG_ERROR_INTERCEPTOR_EXCEPTION, e.toString())
            throw ServiceException.ServerException(ExceptionMessage.SERVICE_FAIL_EXCEPTION)
        }

        when (serviceResult) {
            SUCCESS -> {}

            // Server Exception
            SYSTEM_ERROR, SERVICE_NOT_READY, OVER_REQUEST_LIMIT -> throw ServiceException.ServerException(
                ExceptionMessage.SERVICE_FAIL_EXCEPTION
            )
            // Essential Parameter Exception
            WRONG_POSITION_REQUEST -> throw ServiceException.EssentialParameterException(
                ExceptionMessage.NO_ESSENTIAL_PARAMETER_EXCEPTION
            )
            // Result Exception
            NO_RESULT, NO_RESULT_BUS_ARRIVAL -> throw ServiceException.ResultException(
                ExceptionMessage.NO_RESULT_EXCEPTION
            )
            // Optional Parameter Exception
            else -> throw ServiceException.OptionalParameterException(ExceptionMessage.SERVICE_FAIL_EXCEPTION)
        }
    }

    private fun getServiceResultBy(resultCode: Int) =
        ServiceResult.entries.toTypedArray().single { it.code == resultCode }
}