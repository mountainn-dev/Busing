package com.san.busing.data.source.remote.interceptor

import com.san.busing.data.source.remote.parser.XmlParser
import com.san.busing.data.source.remote.retrofit.ServiceResult.*
import com.san.busing.data.exception.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.InputStream

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

    private fun parseApiResult(inputStream: InputStream) {
        val resultCode = XmlParser().parse(inputStream)

        when (resultCode.toInt()) {
            SUCCESS.code -> {}
            NO_RESULT.code -> throw ApiException.NoResultException(NO_RESULT.message)
            else -> throw RuntimeException()
        }
    }
}