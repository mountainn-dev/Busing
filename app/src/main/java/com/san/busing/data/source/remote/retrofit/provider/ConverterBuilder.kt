package com.san.busing.data.source.remote.retrofit.provider

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import retrofit2.Converter

object ConverterBuilder {
    private val converter =
        TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build())

    fun get(): Converter.Factory = converter
}