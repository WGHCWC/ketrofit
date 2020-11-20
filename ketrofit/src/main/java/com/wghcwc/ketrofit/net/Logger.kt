package com.wghcwc.ketrofit.net

import android.text.TextUtils
import android.util.Log
import com.wghcwc.ketrofit.BuildConfig
import okhttp3.*
import okio.Buffer
import java.io.IOException

class KLoggerInterceptor constructor(
    private val tag: String = "HTTP_TAG", private val showResponse: Boolean = true,
    private val level: Int = INFO,
) : Interceptor {

    companion object {
        const val INFO = 0
        const val DEBUG = 1
        const val WARRING = 2
        const val ERROR = 3
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        logForRequest(request)
        val response = chain.proceed(request)
        return logForResponse(response)
    }

    private fun logForResponse(response: Response): Response {
        try {
            log("========response'log=======")
            val builder = response.newBuilder()
            val clone = builder.build()
            log("|| url : " + clone.request().url())
            log("|| code : " + clone.code())
            log("|| protocol : " + clone.protocol())
            if (!TextUtils.isEmpty(clone.message())) log("|| message : " + clone.message())
            if (showResponse) {
                var body = clone.body()
                if (body != null) {
                    val mediaType = body.contentType()
                    if (mediaType != null) {
                        log("|| responseBody's contentType : $mediaType")
                        if (isText(mediaType)) {
                            val resp = body.string()
                            log("||responseBody's content : $resp")
                            body = ResponseBody.create(mediaType, resp)
                            return response.newBuilder().body(body).build()
                        } else {
                            log("|| responseBody's content : " + " maybe [file part] , too large too print , ignored!")
                        }
                    }
                }
            }
            log("========response'log=======end")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    private fun logForRequest(request: Request) {
        try {
            val url = request.url().toString()
            val headers = request.headers()
            log("========request'log=======")
            log("|| method : " + request.method())
            log("|| url : $url")
            if (headers.size() > 0) {
                log("|| headers : $headers")
            }
            val requestBody = request.body()
            if (requestBody != null) {
                val mediaType = requestBody.contentType()
                if (mediaType != null) {
                    log("|| requestBody's contentType : $mediaType")
                    if (isText(mediaType)) {
                        log("|| requestBody's content : " + bodyToString(request))
                    } else {
                        log("|| requestBody's content : " + " maybe [file part] , too large too print , ignored!")
                    }
                }
            }
            log("========request'log=======end")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isText(mediaType: MediaType): Boolean {
        if (mediaType.type() == "text") {
            return true
        }
        if (mediaType.subtype() == "json" || mediaType.subtype() == "xml" || mediaType.subtype() == "html" || mediaType.subtype() == "webviewhtml") return true
        return false
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "something error when show requestBody."
        }
    }

    private fun log(msg: String) {
        if (BuildConfig.DEBUG.not()) return
        when (level) {
            INFO -> Log.i(tag, msg)
            DEBUG -> Log.d(tag, msg)
            WARRING -> Log.w(tag, msg)
            ERROR -> Log.e(tag, msg)
            else -> Log.i(msg, msg)
        }
    }

}