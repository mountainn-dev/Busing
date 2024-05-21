package com.san.busing.domain.utils

import android.content.Context
import com.san.busing.view.listener.RecyclerViewScrollListener
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object Utils {
    fun getRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(TikXmlConverterFactory.create(getXmlParse()))
        .client(getClient(getInterceptor()))
        .build()

    private fun getInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun getClient(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    private fun getXmlParse() = TikXml.Builder().exceptionOnUnreadXml(false).build()
}