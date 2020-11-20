package com.example.kotlinnet

import android.annotation.SuppressLint
import com.example.kotlinnet.Client.retrofiitClient
import com.wghcwc.ketrofit.net.KLoggerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * author：wghcwc
 * DATE: 20-11-12 16:26
 */


object Client {
    private val trustAllCerts: Array<TrustManager> = arrayOf(
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?,
            ) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?,
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }
        }
    )
    private val sslContext = SSLContext.getInstance("SSL").run {
        init(null, trustAllCerts, SecureRandom())
        this
    }

    fun getBaseUrl(): String {
        var baseurl = "http://10.6.124.120:8080/"
        if (baseurl.endsWith('/').not()) {
            baseurl += '/'
        }
        return baseurl

    }


    private val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

    val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(KLoggerInterceptor())
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()


    val retrofiitClient = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}

val Apis: Service = retrofiitClient.create(Service::class.java)
