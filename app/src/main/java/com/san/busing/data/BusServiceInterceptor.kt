package com.san.busing.data

import com.san.busing.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

object BusServiceInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentUrl = chain.request().url()
        val newUrl = currentUrl.newBuilder().addQueryParameter("serviceKey", BuildConfig.API_KEY).build()
        val currentRequest = chain.request().newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        return chain.proceed(newRequest)
    }
}