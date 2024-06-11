package com.san.busing.data.source.remote.parser

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

/**
 * XmlParser
 *
 * Interceptor 클래스에서 특정 태그의 xml 데이터만을 추출하기 위한 Parser 클래스
 * XmlPullParser 이용
 */
class XmlParser {
    private val ns: String? = null

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): String {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "response")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            if (parser.name == "msgHeader") return readResultCode(parser)
            else skip(parser)
        }

        throw IOException()
    }

    private fun readResultCode(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "msgHeader")

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            if (parser.name == "resultCode") return readText(parser)
            else skip(parser)
        }

        throw IOException()
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }

        return result
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}